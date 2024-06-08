package com.example.reminderapp.api

import com.example.reminderapp.api.model.responses.UserResponse
import com.example.reminderapp.common.networkresult.NetworkResult
import retrofit2.http.GET

interface UserApi {

    @GET("/user")
    suspend fun getUser(
    ): NetworkResult<UserResponse>
}