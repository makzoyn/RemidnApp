package com.example.reminderapp.common.extensions

import android.view.View
import androidx.core.view.isVisible

fun View.toggleAvailability(enabled: Boolean) = run { isEnabled = enabled }
fun View.toggleVisability(visible: Boolean) = run { isVisible = visible}