package com.example.reminderapp.api

data class RemindsResponseItem(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val date: String,
    val alarmId: Int
)