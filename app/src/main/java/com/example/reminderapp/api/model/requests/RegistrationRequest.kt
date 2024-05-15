package com.example.reminderapp.api.model.requests

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String
)
