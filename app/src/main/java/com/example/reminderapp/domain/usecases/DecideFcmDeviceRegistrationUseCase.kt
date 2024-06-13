package com.example.reminderapp.domain.usecases

import com.example.reminderapp.api.model.requests.RegisterDeviceRequest
import com.example.reminderapp.repository.AuthRepository
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import javax.inject.Inject

class DecideFcmDeviceRegistrationUseCase @Inject constructor(
    private val accountRepository: AuthRepository,
    private val preferencesRepository: PreferencesDataStoreRepository,
) {

    suspend operator fun invoke(fcmToken: String? = null, huaweiToken: String? = null) {
        val pushToken = fcmToken ?: huaweiToken ?: accountRepository.getFcmToken() ?: accountRepository.getHmsToken()
        val authToken = preferencesRepository.getToken()
        if (pushToken != null && authToken.isNotEmpty()) {
            accountRepository.registerDevice(RegisterDeviceRequest(fcmToken = pushToken))
        }
    }
}