package com.udacity

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**Created by
Author: Ankush Bose
Date: 02,April,2021
 **/

@HiltAndroidApp
class CustomViewApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}