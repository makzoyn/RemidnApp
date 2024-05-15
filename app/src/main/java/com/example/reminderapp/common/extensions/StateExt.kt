package com.example.reminderapp.common.extensions

import com.example.reminderapp.common.state.State


inline fun <T> State<T>.onSuccess(
    success: (data: T) -> (Unit)
): State<T> = apply {
    if (this is State.Success) {
        success(data)
    }
}

inline fun <T> State<T>.onError(
    error: (error: State.Error) -> (Unit)
): State<T> = apply {
    if (this is State.Error) {
        error(this)
    }
}
