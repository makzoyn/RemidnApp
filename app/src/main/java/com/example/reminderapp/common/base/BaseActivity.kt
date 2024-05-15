package com.example.reminderapp.common.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.reminderapp.R

abstract class BaseActivity(@LayoutRes layoutId: Int) : AppCompatActivity(layoutId) {


    protected open fun doOnFirstCreation(): Unit = Unit


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val callback = ActionBeforeExitCallback(
//            fragmentManager = supportFragmentManager,
//            beforeBackAction = ::beforeBackAction,
//            exitAction = { finish() }
//        )
//
//        onBackPressedDispatcher.addCallback(this, callback)
        if (savedInstanceState == null) {
            doOnFirstCreation()
        }
    }

//    private fun beforeBackAction() {
//        Toast.makeText(this@BaseActivity, getString(R.string.back_pressed_error), Toast.LENGTH_SHORT)
//            .show()
//    }
}