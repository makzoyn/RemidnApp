package com.example.reminderapp.ui.reminds.adapter.viewholders

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.reminderapp.ui.onboarding.OnBoardingViewFragment
import com.example.reminderapp.ui.reminds.main.tabs.notes.MainNotesFragment
import com.example.reminderapp.ui.reminds.main.tabs.reminds.MainRemindsFragment

class ViewPagerOnBoardingAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> OnBoardingViewFragment()
            1 -> OnBoardingViewFragment()
            else -> OnBoardingViewFragment()
        }
    }

}
