package com.example.reminderapp.common.networkresult

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type


class NetworkResultCallAdapter<S : Any>(
    private val successType: Type,
) : CallAdapter<S, Call<NetworkResult<S>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResult<S>> {
        return NetworkResultCall(call)
    }
}