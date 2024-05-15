package com.example.reminderapp.common

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.MenuProvider
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.reminderapp.R
import com.google.android.material.appbar.MaterialToolbar
import java.lang.ref.WeakReference

class ToolbarController(toolbar: MaterialToolbar) {

    private val toolbarRef = WeakReference(toolbar)
    private var menuProvider: MenuProvider? = null

    var title: String? = null

    @ColorRes
    var titleColor: Int = R.color.black

    @DrawableRes
    var navigationIcon: Int? = null

    @ColorRes
    var navigationIconTint: Int = R.color.black
    var navigationIconClick: (() -> Unit) = {}

    var isVisible: Boolean = true

    var onCreateMenu: ((menu: Menu, menuInflater: MenuInflater) -> Unit)? = null
    var onMenuItemSelected: ((menuItem: MenuItem) -> Boolean)? = null
    var onMenuClosed: ((menu: Menu) -> Unit)? = null
    var onPrepareMenu: ((menu: Menu) -> Unit)? = null

    fun invalidate(): ToolbarController {
        title = null
        titleColor = R.color.black
        navigationIcon = null
        navigationIconTint = R.color.black
        navigationIconClick = {}
        isVisible = true

        onCreateMenu = null
        onMenuItemSelected = null
        onPrepareMenu = null
        onMenuClosed = null
        return this
    }

    fun prepare(prepare: ToolbarController.() -> Unit): ToolbarController {
        prepare.invoke(this)
        return this
    }

    fun build(childViewLifecycleOwner: LifecycleOwner?) {
        val toolbar = toolbarRef.get() ?: return
        toolbar.title = title
        toolbar.setTitleTextColor(toolbar.context.getColor(titleColor))
        navigationIcon?.let { toolbar.setNavigationIcon(it) } ?: run {
            toolbar.navigationIcon = null
        }
        toolbar.setNavigationIconTint(toolbar.context.getColor(navigationIconTint))
        toolbar.setNavigationOnClickListener { navigationIconClick() }
        toolbar.isVisible = isVisible
        clearMenuProvider()
        if (childViewLifecycleOwner != null) {
            prepareMenuProvider(toolbar, childViewLifecycleOwner)
        }
    }
    private fun prepareMenuProvider(toolbar: Toolbar, childViewLifecycleOwner: LifecycleOwner) {
        val newMenuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                onCreateMenu?.invoke(menu, menuInflater)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                onMenuItemSelected?.invoke(menuItem) ?: false

            override fun onPrepareMenu(menu: Menu) {
                onPrepareMenu?.invoke(menu)
            }

            override fun onMenuClosed(menu: Menu) {
                onMenuClosed?.invoke(menu)
            }
        }
        toolbar.addMenuProvider(newMenuProvider, childViewLifecycleOwner, Lifecycle.State.RESUMED)
        this.menuProvider = newMenuProvider
    }

    private fun clearMenuProvider() = menuProvider?.let {
        val toolbar = toolbarRef.get()
        toolbar?.removeMenuProvider(it)
    }
}