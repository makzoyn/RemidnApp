package com.example.reminderapp.api.model.responses

import com.example.reminderapp.domain.model.TokenModel
import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token") val token: String
)
fun TokenResponse.mapToDomain() = TokenModel(
    token = token
)