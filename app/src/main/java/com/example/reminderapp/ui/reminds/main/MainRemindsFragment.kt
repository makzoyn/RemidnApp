package com.example.reminderapp.ui.reminds.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.common.extensions.parentToolbar
import com.example.reminderapp.common.extensions.toggleVisability
import com.example.reminderapp.databinding.FragmentMainRemindsBinding
import com.example.reminderapp.ui.reminds.adapter.MainRemindsAdapter
import com.example.reminderapp.ui.reminds.adapter.MainRemindsSelectionAdapter
import com.example.reminderapp.ui.reminds.adapter.decoration.RemindsItemDecoration
import com.example.reminderapp.ui.reminds.adapter.model.RemindItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainRemindsFragment : BaseFragment(R.layout.fragment_main_reminds) {
    private val viewModel: MainRemindsViewModel by viewModels<MainRemindsViewModelImpl>()
    private val binding by viewBinding(FragmentMainRemindsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        preparePromotionsRecycler()
        bindUi()
        bindToolbar()
    }


    private val remindsAdapter: MainRemindsAdapter by lazy {
        MainRemindsAdapter(
            remindClick = { remindId ->
                viewModel.editRemind(remindId)
            },
            longClick = { remindId ->
                viewModel.selectedChanged(remindId)
                swapRVAdapted()
            }
        )
    }

    private val remindsSelectionAdapter: MainRemindsSelectionAdapter by lazy {
        MainRemindsSelectionAdapter(
            remindClick = { remindId ->
                viewModel.selectedChanged(remindId)
            },
            longClick = { remindId ->
                swapRVAdapted()
            }
        )
    }

    private fun bindUi() {
        binding.deleteRemindsButton.setOnClickListener {
            viewModel.deleteSelectedReminds()
        }
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refreshReminds()
        }
        binding.addRemindButton.setOnClickListener {
            viewModel.createRemindClicked()
        }
    }

    private fun bindToolbar() {
        parentToolbar {
            isVisible = true
            title = getString(R.string.reminds_title)
            navigationIcon = R.drawable.ic_check
            navigationIconClick = {
                swapRVAdapted()
            }
        }
    }

    private fun bindAdapter(reminds: List<RemindItem>?) {
        reminds?.let {
            remindsAdapter.submitList(reminds)
            remindsSelectionAdapter.submitList(reminds)
        }
    }

    private fun observeViewModel() = with(viewModel) {
        loadingState.listenValue {
            if (binding.swipeToRefresh.isRefreshing) {
                binding.progressLayout.toggleVisability(false)
                binding.swipeToRefresh.isRefreshing = it
            } else {
                binding.progressLayout.toggleVisability(it)
                binding.swipeToRefresh.isRefreshing = false
            }
        }
        remindsData.listenValue(::bindAdapter)
        navigationFlow.listenValue(::onNavigate)
        deleteVisibilityState.listenValue(binding.deleteRemindsButton::toggleVisability)
    }

    private fun preparePromotionsRecycler() {
        val lm =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        with(binding.remindsData) {
            layoutManager = lm
            adapter = remindsAdapter
            addItemDecoration(RemindsItemDecoration())
        }
    }

    private fun swapRVAdapted() = with(binding.remindsData) {
        if (adapter is MainRemindsAdapter) {
            swapAdapter(remindsSelectionAdapter, true)
        } else {
            viewModel.unselectAll()
            swapAdapter(remindsAdapter, true)
        }
    }

    private fun onNavigate(pair: Pair<Int?, Any?>?) {
        pair?.first?.let {
            navigate(it, pair.second)
        }
    }
}