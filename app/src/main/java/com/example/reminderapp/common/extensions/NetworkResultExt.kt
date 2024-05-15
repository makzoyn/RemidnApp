package com.example.reminderapp.common.extensions

import com.example.reminderapp.common.networkresult.NetworkError
import com.example.reminderapp.common.networkresult.NetworkException
import com.example.reminderapp.common.networkresult.NetworkResult
import com.example.reminderapp.common.networkresult.NetworkSuccess
import com.example.reminderapp.common.state.State

fun <T : Any> NetworkResult<T>.mapToState(): State<T> =
    when (this) {
        is NetworkSuccess -> State.Success(this.body)
        is NetworkError -> State.Error.NetworkError(this.code, this.message)
        is NetworkException -> State.Error.NetworkException(this.e.message ?: "")
    }

fun <SuccessResp : Any, SuccessDomain : Any>
        NetworkResult<SuccessResp>.mapResult(
    successMapper: (SuccessResp) -> SuccessDomain
): NetworkResult<SuccessDomain> {

    return when (this) {
        is NetworkSuccess -> NetworkSuccess(successMapper(body))
        is NetworkError -> NetworkError(code, message)
        is NetworkException -> NetworkException(e)
    }
}