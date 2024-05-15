package com.example.reminderapp.api.interceptors

import com.example.reminderapp.repository.PreferencesDataStoreRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val preferenceRepository: PreferencesDataStoreRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val reqBuilder = chain.request().newBuilder()

        val token = runBlocking {
            preferenceRepository.getToken()
        }

        if (token.isNotEmpty() && chain.request().header(AUTH_HEADER_VALUE_PREFIX) == null) {
            reqBuilder.addHeader(AUTH_HEADER_VALUE_PREFIX, token)
        }

        return chain.proceed(reqBuilder.build())
    }

    companion object {
        const val AUTH_HEADER_VALUE_PREFIX = "Bearer-Authorization"
    }
}