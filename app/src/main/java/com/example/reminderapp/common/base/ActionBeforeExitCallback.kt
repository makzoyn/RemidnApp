package com.example.reminderapp.common.base

import android.os.SystemClock
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager

class ActionBeforeExitCallback(
    private val fragmentManager: FragmentManager,
    private val beforeBackAction: () -> Unit,
    private val exitAction: () -> Unit
) : OnBackPressedCallback(true) {

    private var actionInvoked = false

    private val interval = 2000L
    private var lastActionInvokedTime = 0L

    override fun handleOnBackPressed() {
        val timePassed = SystemClock.elapsedRealtime() - lastActionInvokedTime
        if (timePassed > interval) {
            actionInvoked = false
        }
        if (actionInvoked) {
            actionInvoked = false
            if (fragmentManager.backStackEntryCount == 0) {
                exitAction()
            }
        } else {
            beforeBackAction()
            actionInvoked = true
            lastActionInvokedTime = SystemClock.elapsedRealtime()
        }
    }
}