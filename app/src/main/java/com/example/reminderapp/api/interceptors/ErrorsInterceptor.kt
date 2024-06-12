package com.example.reminderapp.api.interceptors

import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.example.reminderapp.singleresult.NetworkErrorEvents
import com.example.reminderapp.singleresult.NetworkErrorResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ErrorsInterceptor @Inject constructor(
    private val networkErrorResult: NetworkErrorResult,
    private val preferencesDataStore: PreferencesDataStoreRepository
    ) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {

            when (response.code) {
                401 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        preferencesDataStore.clearPreferences()
                        networkErrorResult.postEvent(NetworkErrorEvents.TokenError)
                    }
                }

                else -> {
                    return response
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                preferencesDataStore.updateInternetConnectionState(true)
            }
        }
        return response
    }
}
