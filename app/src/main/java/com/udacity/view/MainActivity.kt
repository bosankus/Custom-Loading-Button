package com.udacity.view

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.udacity.R
import com.udacity.databinding.ActivityMainBinding
import com.udacity.util.DownloadStatus
import com.udacity.util.downloadManager
import com.udacity.util.showNotification
import com.udacity.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private lateinit var binding: ActivityMainBinding

    private var downloadContentObserver: ContentObserver? = null
    private var downloadFileName = ""
    private var downloadID: Long = 0
    private lateinit var action: NotificationCompat.Action


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            setSupportActionBar(toolbar)
            registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            onClickListeners()
        }
    }


    // click listener for download button
    private fun ActivityMainBinding.onClickListeners() {
        with(contentMain) {
            customButton.setOnClickListener {
                when (downloadOptionRadioGroup.checkedRadioButtonId) {
                    View.NO_ID -> {
                        DownloadStatus.UNKNOWN
                        showToast(resources.getString(R.string.no_file_selection))
                    }
                    else -> {
                        downloadFileName =
                            findViewById<RadioButton>(downloadOptionRadioGroup.checkedRadioButtonId)
                                .text.toString()
                        customButton.startLoading()
                        startDownload()
                    }
                }
            }
        }
    }


    // receive broadcast once download is completed
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            id?.let {
                val downloadStatus = downloadManager().getStatus(it)
                downloadContentObserver?.unregister()
                downloadContentObserver = null
                downloadStatus.takeIf { status -> status != DownloadStatus.UNKNOWN }?.run {
                    showNotification(
                        context = applicationContext,
                        status = downloadStatus,
                        fileName = downloadFileName,
                        notificationManager = notificationManager,
                        notificationBuilder = notificationBuilder
                    )
                }
            }
        }
    }


    // method to know the status of query made to download manager
    private fun DownloadManager.getStatus(id: Long): DownloadStatus {
        query(DownloadManager.Query().setFilterById(id)).use {
            with(it) {
                if (this != null && moveToFirst()) {
                    return when (getInt(getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.SUCCESSFUL
                        DownloadManager.STATUS_FAILED -> DownloadStatus.FAILED
                        else -> DownloadStatus.UNKNOWN
                    }
                }
                return DownloadStatus.UNKNOWN
            }
        }
    }


    // method to check download query progress
    private fun DownloadManager.checkQueryProgress() {
        query(DownloadManager.Query().setFilterById(downloadID)).use {
            with(it) {
                if (this != null && moveToFirst()) {
                    when (getInt(getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_FAILED -> {
                            binding.contentMain.customButton.stopLoading()
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            binding.contentMain.customButton.stopLoading()
                        }
                    }
                }
            }
        }
    }


    // method to register content observer
    private fun DownloadManager.registerContentObserver() {
        object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                downloadContentObserver?.run { checkQueryProgress() }
            }
        }.also {
            downloadContentObserver = it
            contentResolver.registerContentObserver(
                "content://downloads/my_downloads".toUri(),
                true,
                downloadContentObserver!!
            )
        }
    }


    // method to unregister content observer
    private fun ContentObserver.unregister() {
        contentResolver.unregisterContentObserver(this)
    }


    // method to start download
    private fun startDownload() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        downloadManager.registerContentObserver()
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
    }

}
