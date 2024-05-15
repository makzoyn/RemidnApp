package com.example.reminderapp.api.model.requests

import com.google.gson.annotations.SerializedName

data class RegisterDeviceRequest(
    @SerializedName("fcmToken") val fcmToken: String
)
