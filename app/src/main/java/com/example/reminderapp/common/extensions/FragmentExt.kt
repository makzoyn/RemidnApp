package com.example.reminderapp.common.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.reminderapp.common.ToolbarController
import com.example.reminderapp.common.ToolbarControllerProvider

fun Fragment.parentToolbar(
    childViewLifecycleOwner: LifecycleOwner? = null,
    prepare: (ToolbarController.() -> Unit)
) {
    (activity as? ToolbarControllerProvider)
        ?.provideToolbarController()
        ?.invalidate()
        ?.prepare(prepare)
        ?.build(childViewLifecycleOwner)
}