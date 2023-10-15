package com.example.reminderapp.alarm

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reminderapp.R

import com.example.reminderapp.databinding.NotificationMessageBinding

class NotificationMessage: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.notification_message)
        val binding = NotificationMessageBinding.inflate(layoutInflater)
        val bundle = intent.extras
        binding.tvMessage.text = bundle?.getString("message")
    }
}