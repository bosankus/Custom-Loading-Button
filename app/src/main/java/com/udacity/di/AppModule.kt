package com.udacity.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.util.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**Created by
Author: Ankush Bose
Date: 02,April,2021
 **/

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesNotificationManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext app: Context,
    ): NotificationCompat.Builder = NotificationCompat.Builder(
        app,
        NOTIFICATION_CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_assistant)

}