package com.example.reminderapp.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ViewCustomTabBinding
import com.google.android.material.tabs.TabLayout

class CustomTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr
) {
    private val binding: ViewCustomTabBinding
    init {
        binding = ViewCustomTabBinding.inflate(LayoutInflater.from(context), this)
        setBackgroundResource(R.drawable.back_select)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTab)
        val firstTabText = typedArray.getString(R.styleable.CustomTab_first_text)
        val secondTabText = typedArray.getString(R.styleable.CustomTab_second_text)
        typedArray.recycle()

        firstTabText?.let { setTabTextByIndex(it, 0) }
        secondTabText?.let { setTabTextByIndex(it, 1) }
    }

    fun setSelection(position: Int) {
        binding.tabs.getTabAt(position)?.select() ?: 0
    }

    fun addOnTabSelectedListener(listener: TabLayout.OnTabSelectedListener) =
        binding.tabs.addOnTabSelectedListener(listener)

    fun removeOnTabSelectedListener(listener: TabLayout.OnTabSelectedListener) =
        binding.tabs.removeOnTabSelectedListener(listener)

    private fun setTabTextByIndex(text: String, index: Int) {
        binding.tabs.getTabAt(index)?.text = text
    }
}