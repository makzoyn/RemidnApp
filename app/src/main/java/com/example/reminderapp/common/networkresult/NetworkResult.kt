package com.example.reminderapp.common.networkresult

import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response


sealed interface NetworkResult<T : Any>

class NetworkSuccess<T : Any>(val body: T) : NetworkResult<T>
data class NetworkError<T : Any>(val code: Int, val message: String) : NetworkResult<T>

data class NetworkException<T : Any>(val e: Throwable) : NetworkResult<T>

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkSuccess(body)
        } else {
            NetworkError(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        NetworkError(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkException(e)
    }
}