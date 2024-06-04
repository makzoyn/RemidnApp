package com.example.reminderapp.singleresult

class NetworkErrorResult : SingleResult<NetworkErrorEvents>() {
    internal suspend fun postEvent(result: NetworkErrorEvents) {
        postEventInternal(result)
    }
}

sealed class NetworkErrorEvents {
    data class ShowErrorDialog(val title: String, val message: String) : NetworkErrorEvents()
    data object TokenError : NetworkErrorEvents()
}