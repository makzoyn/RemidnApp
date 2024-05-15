package com.example.reminderapp.common.extensions

import androidx.lifecycle.SavedStateHandle

fun <T> SavedStateHandle.requireParam(): T {
    return get<T>("param")!!
}