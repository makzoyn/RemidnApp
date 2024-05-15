package com.example.reminderapp.api

import com.example.reminderapp.api.model.requests.DeletePushRequest
import com.example.reminderapp.api.model.requests.LoginRequest
import com.example.reminderapp.api.model.requests.RegisterDeviceRequest
import com.example.reminderapp.api.model.requests.RegistrationRequest
import com.example.reminderapp.api.model.responses.TokenResponse
import com.example.reminderapp.common.networkresult.NetworkResult
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/register")
    suspend fun registrationUser(
        @Body registrationRequest: RegistrationRequest
    ): NetworkResult<TokenResponse>

    @POST("/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): NetworkResult<TokenResponse>

    @POST("/device/register")
    suspend fun registerDevice(
        @Body registerDeviceRequest: RegisterDeviceRequest
    ): NetworkResult<TokenResponse>

    @POST("/devices/delete-after-push")
    suspend fun deleteRemindAfterPush(
        @Body deletePushRequest: DeletePushRequest
    ): NetworkResult<Unit>
}