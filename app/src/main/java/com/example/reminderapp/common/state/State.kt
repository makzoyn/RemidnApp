package com.example.reminderapp.common.state

sealed class State<out T> {

    object Idle : State<Nothing>()

    data class Success<T>(val data: T) : State<T>()

    object Loading : State<Nothing>()


    sealed class Error(open val errorMessage: String) : State<Nothing>() {

        data class NetworkError(val code: Int, override val errorMessage: String): Error(errorMessage)
        data class NetworkException(override val errorMessage: String): Error(errorMessage)
    }


    fun isLoading(): Boolean = this is Loading && this !is Error

}