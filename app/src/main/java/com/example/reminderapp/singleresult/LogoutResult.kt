package com.example.reminderapp.singleresult

class LogoutResult : SingleResult<LogoutEvents>() {
    internal suspend fun postEvent(result: LogoutEvents) {
        postEventInternal(result)
    }
}

sealed class LogoutEvents {
    data object Logout : LogoutEvents()
}