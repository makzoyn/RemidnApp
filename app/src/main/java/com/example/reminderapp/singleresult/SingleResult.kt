package com.example.reminderapp.singleresult

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

open class SingleResult<T> {
    private val _events = Channel<T>()
    val events = _events.receiveAsFlow()

    protected suspend fun postEventInternal(result: T) {
        _events.send(result)
    }
}