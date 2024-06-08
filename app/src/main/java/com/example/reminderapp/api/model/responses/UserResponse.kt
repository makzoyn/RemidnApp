package com.example.reminderapp.api.model.responses

import com.example.reminderapp.domain.model.UserModel
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("login") val login: String,
    @SerializedName("email") val email: String?
)

fun UserResponse.toDomain() = UserModel(
    login = login,
    email = email
)
