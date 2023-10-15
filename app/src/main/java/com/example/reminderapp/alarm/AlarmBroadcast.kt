package com.example.reminderapp.alarm


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.reminderapp.R


class AlarmBroadcast : BroadcastReceiver() {
    @SuppressLint("RemoteViewLayout")
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.extras
        val text = bundle?.getString("event")
        val date = bundle?.getString("date") + " " + bundle?.getString("time")
        val description = bundle?.getString("description")

        val intent1 = Intent(context, NotificationMessage::class.java)
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent1.putExtra("message", text)
        //Notification builder
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                1,
                intent1,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        /*val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(context!!, "notify_001")
        //val contentView = RemoteViews(context.packageName, R.layout.notification_layout)
        //contentView.setImageViewResource(R.id.icon, R.mipmap.ic_launcher)

        val pendingSwitchIntent = PendingIntent.getBroadcast(
            context, 0,
            intent!!, PendingIntent.FLAG_IMMUTABLE
        )
        //contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent)
        //contentView.setTextViewText(R.id.message, text)
       // contentView.setTextViewText(R.id.date, date)
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background)
        mBuilder.setAutoCancel(true)
        mBuilder.setContentText(text)
        mBuilder.setSubText(date)
        mBuilder.setOngoing(true)
        NotificationCompat.PRIORITY_HIGH
        mBuilder.setOnlyAlertOnce(true)
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR
        //mBuilder.setContent(contentView)
        mBuilder.setContentIntent(pendingIntent)


        val channelId = "channel_id"
        val channel =
            NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
        mBuilder.setChannelId(channelId)

        val notification = mBuilder.build()
        notificationManager.notify(1, notification)*/
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "channel_id"
        val channelName = "channel_name"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelDescription = "channel_description"
            val channelGroup = NotificationChannelGroup("group_id", "group_name")
            notificationManager.createNotificationChannelGroup(channelGroup)
            channel.group = channelGroup.id
            channel.description = channelDescription
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val notificationManagerCompat = NotificationManagerCompat.from(context!!)
            val areNotificationsEnabled =
                notificationManagerCompat.areNotificationsEnabled()
            if (!areNotificationsEnabled) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(context, intent, null)
            }
        }
        val pendingSwitchIntent = PendingIntent.getBroadcast(
            context, 0,
            intent!!, PendingIntent.FLAG_IMMUTABLE
        )
        val contentView = RemoteViews(context.packageName, R.layout.notification_layout)
        contentView.setImageViewResource(R.id.icon, R.mipmap.ic_launcher)
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent)
        contentView.setTextViewText(R.id.message, text)
        contentView.setTextViewText(R.id.description, description)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setContent(contentView)
            .setChannelId(channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, builder.build())
    }
}