package com.example.reminderapp.common.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import com.example.reminderapp.R

object DialogUtils {

    fun createSimpleOkErrorDialog(context: Context, title: String, message: String): Dialog {
        val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ок", null)
        return alertDialog.create()
    }
}