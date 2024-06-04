package com.example.reminderapp.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.parentToolbar
import com.example.reminderapp.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment(R.layout.fragment_profile) {
    private val viewModel: ProfileViewModel by viewModels<ProfileViewModelImpl>()
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUi()
        observeViewModel()
    }



    private fun prepareUi() = with(binding) {
        logout.setOnClickListener {

        }
        editProfile.setOnClickListener {

        }
        parentToolbar {
            isVisible = true
            navigationIcon = R.drawable.ic_back
            navigationIconClick = {
                popBack()
            }
            title = getString(R.string.remind_create_title)
        }
    }

    private fun observeViewModel() = with(viewModel) {

    }
}