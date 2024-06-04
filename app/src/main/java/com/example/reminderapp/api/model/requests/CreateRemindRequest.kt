package com.example.reminderapp.api.model.requests

import com.google.gson.annotations.SerializedName

data class CreateRemindRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("need_to_notified") val needToNotified: Boolean
)
