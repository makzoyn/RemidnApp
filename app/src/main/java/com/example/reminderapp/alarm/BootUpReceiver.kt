package com.example.reminderapp.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.reminderapp.ui.MainActivity

class BootUpReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }
}