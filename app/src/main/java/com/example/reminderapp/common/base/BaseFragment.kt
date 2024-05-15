package com.example.reminderapp.common.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BaseFragment : Fragment {
    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    protected open fun doOnFirstCreation(): Unit = Unit

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            doOnFirstCreation()
        }
    }

    protected fun navigate(navDirections: Int, args: Any? = null) {
        if(args == null) {
            findNavController().navigate(navDirections)
        } else {
            findNavController().navigate(
                navDirections, bundleOf(
                    "param" to args
                )
            )
        }
    }

    protected fun popBack() {
        findNavController().popBackStack()
    }
}