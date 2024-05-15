package com.example.reminderapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    private fun createChannel() {
        val defaultChannel = NotificationChannel(
            "default_channel_id",
            "defaultChannelName",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(this).createNotificationChannel(defaultChannel)

    }
}