package com.example.reminderapp.singleresult

class ReceiveMessageFromPushResult: SingleResult<ReceiveMessageFromPushEvent>() {
    internal suspend fun postEvent(result: ReceiveMessageFromPushEvent) {
        postEventInternal(result)
    }
}
sealed class ReceiveMessageFromPushEvent {
    data class ReceivePushEvent(val id: Int) : ReceiveMessageFromPushEvent()
    data object RemindReceived: ReceiveMessageFromPushEvent()
}