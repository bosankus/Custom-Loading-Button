package com.udacity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.udacity.R
import com.udacity.util.DownloadStatus
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
    }

    companion object {
        private const val EXTRA_FILE_NAME = "DOWNLOADED_FILE_NAME"
        private const val EXTRA_DOWNLOAD_STATUS = "DOWNLOAD_STATUS"

        fun bundleExtrasOf(
            status: DownloadStatus,
            fileName: String
        ) = bundleOf(
            EXTRA_FILE_NAME to fileName,
            EXTRA_DOWNLOAD_STATUS to status .statusText
        )
    }
}
