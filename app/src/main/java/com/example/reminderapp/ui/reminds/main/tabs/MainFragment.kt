package com.example.reminderapp.ui.reminds.main.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.TabSelectedListener
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.databinding.FragmentMainBinding
import com.example.reminderapp.ui.reminds.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: BaseFragment(R.layout.fragment_main), TabSelectedListener {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }
    private fun observeViewModel() = with(viewModel) {
        tabSelection.listenValue(::prepareViewPager)
    }
    private fun prepareViewPager(position: Int) {
        val adapter = ViewPagerAdapter(this, position)
        binding.viewPager.adapter = adapter
        binding.customTab.addOnTabSelectedListener(this)
        binding.customTab.setSelection(position)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.customTab.removeOnTabSelectedListener(this)
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        viewModel.tabSelectionChanged(p0?.position ?: 0)
    }

}