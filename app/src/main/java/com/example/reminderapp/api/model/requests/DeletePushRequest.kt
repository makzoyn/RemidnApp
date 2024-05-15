package com.example.reminderapp.api.model.requests

import com.google.gson.annotations.SerializedName

data class DeletePushRequest(
    @SerializedName("id") val id: Int
)
