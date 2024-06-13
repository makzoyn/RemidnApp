package com.example.reminderapp.repository

import com.example.reminderapp.api.AuthApi
import com.example.reminderapp.api.model.requests.DeletePushRequest
import com.example.reminderapp.api.model.requests.LoginRequest
import com.example.reminderapp.api.model.requests.RegisterDeviceRequest
import com.example.reminderapp.api.model.requests.RegistrationRequest
import com.example.reminderapp.api.model.responses.mapToDomain
import com.example.reminderapp.common.extensions.mapResult
import com.example.reminderapp.common.networkresult.NetworkResult
import com.example.reminderapp.domain.model.TokenModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun registrationUser(
        registrationRequest: RegistrationRequest
    ): NetworkResult<TokenModel>

    suspend fun loginUser(
        loginRequest: LoginRequest
    ): NetworkResult<TokenModel>

    suspend fun registerDevice(
        registerDeviceRequest: RegisterDeviceRequest
    ): NetworkResult<TokenModel>

    suspend fun getFcmToken(): String?
    suspend fun getHmsToken(): String?

    suspend fun deletePush(
        deletePushRequest: DeletePushRequest
    ): NetworkResult<Unit>
}

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun registrationUser(
        registrationRequest: RegistrationRequest
    ) = api.registrationUser(registrationRequest).mapResult { it.mapToDomain() }

    override suspend fun loginUser(
        loginRequest: LoginRequest
    ) = api.loginUser(loginRequest).mapResult { it.mapToDomain() }

    override suspend fun registerDevice(
        registerDeviceRequest: RegisterDeviceRequest
    ) = api.registerDevice(registerDeviceRequest).mapResult { it.mapToDomain() }

    override suspend fun getFcmToken(): String? {
        return try {
            FirebaseMessaging.getInstance()
                .token
                .await()
        } catch (e: java.lang.Exception) {
            null
        }
    }

    override suspend fun getHmsToken(): String? {
        return try {
            HmsMessageService.getInstance()
                .token
                .await()
        } catch (e: java.lang.Exception) {
            null
        }
    }

    override suspend fun deletePush(
        deletePushRequest: DeletePushRequest
    ): NetworkResult<Unit> =
        api.deleteRemindAfterPush(deletePushRequest)
}