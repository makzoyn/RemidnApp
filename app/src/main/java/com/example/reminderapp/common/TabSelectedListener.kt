package com.example.reminderapp.common

import com.google.android.material.tabs.TabLayout

interface TabSelectedListener : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(p0: TabLayout.Tab?) {}

    override fun onTabUnselected(p0: TabLayout.Tab?) {}

    override fun onTabReselected(p0: TabLayout.Tab?) {}
}