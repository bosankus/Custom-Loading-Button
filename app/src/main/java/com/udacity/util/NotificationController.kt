package com.udacity.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.view.DetailActivity

/**Created by
Author: Ankush Bose
Date: 02,April,2021
 **/

// method to show notification
fun showNotification(
    context: Context,
    notificationTitle: String = context.resources.getString(R.string.default_noti_title),
    notificationBody: String = context.resources.getString(R.string.default_noti_body),
    notificationId: Int = NOTIFICATION_ID.hashCode(),
    status: DownloadStatus,
    fileName: String,
    notificationManager: NotificationManager,
    notificationBuilder: NotificationCompat.Builder
) {

    // Creating notification channel
    if (DEVICE_ANDROID_VERSION >= ANDROID_OREO)
        createNotificationChannel(notificationManager)

    notificationBuilder.apply {
        setContentTitle(notificationTitle)
        setContentText(notificationBody)
        setAutoCancel(true)
        addAction(
            R.drawable.ic_assistant,
            context.resources.getString(R.string.default_noti_action),
            generatePendingIntent(context, status, fileName)
        )
    }
    notificationManager.notify(notificationId, notificationBuilder.build())
}


// method to create pending Intent
private fun generatePendingIntent(
    context: Context,
    status: DownloadStatus,
    fileName: String,
): PendingIntent {
    val intent = Intent(context, DetailActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtras(DetailActivity.bundleExtrasOf(status, fileName))
    }
    return PendingIntent.getActivities(
        context,
        NOTIFICATION_REQUEST_CODE,
        arrayOf(intent),
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}


// method to create notification channel
private fun createNotificationChannel(notificationManager: NotificationManager) {
    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        lightColor = Color.YELLOW
        enableVibration(true)
        setShowBadge(true)
    }
    notificationManager.createNotificationChannel(channel)
}
