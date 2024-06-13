package com.example.reminderapp.api.model.requests

import com.google.gson.annotations.SerializedName

data class RegisterDeviceHmsRequest(
    @SerializedName("hmsToken") val hmsToken: String
)
