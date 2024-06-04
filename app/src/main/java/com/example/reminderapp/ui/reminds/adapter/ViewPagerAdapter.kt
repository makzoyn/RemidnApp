package com.example.reminderapp.ui.reminds.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.reminderapp.ui.reminds.main.tabs.notes.MainNotesFragment
import com.example.reminderapp.ui.reminds.main.tabs.reminds.MainRemindsFragment

class ViewPagerAdapter(
    fragment: Fragment,
    private val tapPosition: Int
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(tapPosition) {
            0 -> MainRemindsFragment()
            1 -> MainNotesFragment()
            else -> MainRemindsFragment()
        }
    }

}
