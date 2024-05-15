package com.example.reminderapp.api.model.requests

import com.google.gson.annotations.SerializedName

data class DeleteRemindsRequest(
    @SerializedName("ids") val ids: List<Int>
)
