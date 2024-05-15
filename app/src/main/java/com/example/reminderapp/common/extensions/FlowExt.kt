package com.example.reminderapp.common.extensions

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

context(Fragment)
fun <T> Flow<T>.listenValue(action: suspend (T) -> Unit) {
    onEach { action.invoke(it) }
        .launchIn(viewLifecycleOwner.lifecycleScope)
}

context(AppCompatActivity)
fun <T> Flow<T>.listenValue(action: suspend (T) -> Unit) {
    onEach { action.invoke(it) }
        .launchIn(lifecycleScope)
}