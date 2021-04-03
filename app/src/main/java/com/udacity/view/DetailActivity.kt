package com.udacity.view

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.udacity.R
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.databinding.ContentDetailBinding
import com.udacity.util.DownloadStatus
import com.udacity.util.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 28, March,2021
 **/

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationManager: NotificationManager

    private lateinit var binding: ActivityDetailBinding

    private val fileName by lazy {
        intent?.extras?.getString(EXTRA_FILE_NAME, "") ?: ""
    }
    private val downloadStatus by lazy {
        intent?.extras?.getString(EXTRA_DOWNLOAD_STATUS, "") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        binding.apply {
            setSupportActionBar(toolbar)
            setUpUI()
            // cancel the notification from status bar
            notificationManager.cancel(NOTIFICATION_ID.hashCode())
        }
    }

    private fun ActivityDetailBinding.setUpUI() {
        with(contentDetail) {
            fileNameText.text = fileName
            downloadStatusText.text = downloadStatus
            okButton.setOnClickListener { goToMainActivity() }
            changeDownloadStatus()
        }
    }


    private fun ContentDetailBinding.changeDownloadStatus() {
        when (downloadStatusText.text) {
            DownloadStatus.SUCCESSFUL.statusText -> {
                changeDownloadIconStatus(R.drawable.ic_check_circle_outline_24)
                changeDownloadStatusColorTo(R.color.colorPrimaryDark)
            }

            DownloadStatus.FAILED.statusText -> {
                changeDownloadIconStatus(R.drawable.ic_error_24)
                changeDownloadStatusColorTo(android.R.color.holo_red_light)
            }
        }
    }


    private fun ContentDetailBinding.changeDownloadIconStatus(@DrawableRes img: Int) {
        downloadStatusImage.setImageResource(img)
    }


    private fun ContentDetailBinding.changeDownloadStatusColorTo(@ColorRes color: Int) {
        ContextCompat.getColor(this@DetailActivity, color)
            .also {
                downloadStatusText.setTextColor(it)
            }
    }


    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }


    override fun onBackPressed() {
        super.onBackPressed()
        goToMainActivity()
    }


    companion object {
        private const val EXTRA_FILE_NAME = "DOWNLOADED_FILE_NAME"
        private const val EXTRA_DOWNLOAD_STATUS = "DOWNLOAD_STATUS"

        fun bundleExtrasOf(
            status: DownloadStatus,
            fileName: String,
        ) = bundleOf(
            EXTRA_FILE_NAME to fileName,
            EXTRA_DOWNLOAD_STATUS to status.statusText,
        )
    }
}
