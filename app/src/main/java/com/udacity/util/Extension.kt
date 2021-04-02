package com.udacity.util

import android.animation.AnimatorSet
import android.app.DownloadManager
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat

/**Created by
Author: Ankush Bose
Date: 29,March,2021
 **/

fun AnimatorSet.disableView(view: View) = apply {
    doOnStart { view.isEnabled = false }
    doOnEnd { view.isEnabled = true }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.downloadManager(): DownloadManager = ContextCompat.getSystemService(
    this,
    DownloadManager::class.java
) as DownloadManager