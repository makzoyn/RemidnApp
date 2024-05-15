package com.example.reminderapp.fcm

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.media.RingtoneManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reminderapp.R
import com.example.reminderapp.domain.usecases.DecideFcmDeviceRegistrationUseCase
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.example.reminderapp.singleresult.ReceiveMessageFromPushEvent
import com.example.reminderapp.singleresult.ReceiveMessageFromPushResult
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@AndroidEntryPoint
class FcmMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var repository: PreferencesDataStoreRepository


    @Inject
    lateinit var decideFcmDeviceRegistration: DecideFcmDeviceRegistrationUseCase

    @Inject
    lateinit var receiveMessageFromPushResult: ReceiveMessageFromPushResult

    private val job = SupervisorJob()
    private var msgId = AtomicInteger()

    override fun onNewToken(newFCMToken: String) {
        val errorHandling = CoroutineExceptionHandler { _, err ->
            err.printStackTrace()
        }
        CoroutineScope(job + errorHandling).launch {
            repository.updateFirebaseToken(newFCMToken)
            decideFcmDeviceRegistration(newFCMToken)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notificationId = msgId.getAndIncrement()
        val notification = createSimpleNotification(this, message.notification)
        val id = message.data["id"]
        id?.toInt()?.let {
            CoroutineScope(Dispatchers.IO).launch {
                receiveMessageFromPushResult.postEvent(
                    ReceiveMessageFromPushEvent.ReceivePushEvent(it)
                )
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this)
            .notify(notificationId, notification)

    }

    private fun createSimpleNotification(
        context: Context,
        notification: RemoteMessage.Notification?
    ): Notification = NotificationCompat.Builder(
        context,
        "default_channel_id"
    )
        .setContentTitle(notification?.title)
        .setContentText(notification?.body)
        .setSmallIcon(R.drawable.img_login)
        .setDefaults(Notification.DEFAULT_ALL)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setAutoCancel(true)
        .setShowWhen(true)
        .build()

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}