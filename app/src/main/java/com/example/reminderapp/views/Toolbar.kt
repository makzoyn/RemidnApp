package com.example.reminderapp.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.withStyledAttributes
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ToolbarBinding

class Toolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private val binding = ToolbarBinding.inflate(LayoutInflater.from(context), this)

    init {
        context.withStyledAttributes(attrs, R.styleable.Toolbar) {
            val text = getString(R.styleable.Toolbar_android_text).orEmpty()
            val startIcon = getDrawable(R.styleable.Toolbar_startIcon)
            val endIcon = getDrawable(R.styleable.Toolbar_endIcon)
            updateText(text)
            updateStartIcon(startIcon)
            updateEndIcon(endIcon)

        }
    }

    fun updateText(text: String) {
        binding.title.text = text
    }

    fun updateStartIcon(icon: Drawable?) {
        binding.startIcon.setImageDrawable(icon)
    }

    fun updateEndIcon(icon: Drawable?) {
        binding.endIcon.setImageDrawable(icon)
    }

    fun setNavigationActionToStartIcon(action: () -> Unit) {
        binding.startIcon.setOnClickListener { action.invoke() }
    }

    fun setNavigationActionToEndIcon(action: () -> Unit) {
        binding.endIcon.setOnClickListener { action.invoke() }
    }
}