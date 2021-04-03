package com.udacity.util

import android.os.Build

/**Created by
Author: Ankush Bose
Date: 27,March,2021
 **/

// For notification channel
const val NOTIFICATION_CHANNEL_ID = "custom_channel"
const val NOTIFICATION_CHANNEL_NAME = "custom"
const val NOTIFICATION_ID = "custom_id"
const val NOTIFICATION_REQUEST_CODE = 100
const val ACTION_NOTIFICATION = "notification_action"

// For Android version check
val DEVICE_ANDROID_VERSION = Build.VERSION.SDK_INT
const val ANDROID_OREO = Build.VERSION_CODES.O
