package com.example.reminderapp.domain.model

data class RemindModel(
    val id: Int,
    val title: String,
    val description: String?,
    val time: String?,
    val date: String?,
    val isNotified: Boolean,
    val needToNotified: Boolean
)