package com.example.reminderapp.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.common.extensions.parentToolbar
import com.example.reminderapp.databinding.FragmentProfileBinding
import com.example.reminderapp.domain.model.UserModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            viewModel.logOut()
        }
        editProfile.setOnClickListener {
            viewModel.changeEditState()
        }
        binding.toolbar.setNavigationActionToStartIcon {
            popBack()
        }
    }

    private fun bindUserData(userModel: UserModel?) {
        binding.email.setText(userModel?.email)
        binding.login.setText(userModel?.login)
    }

    private fun bindEditState(state: Boolean) {
        binding.emailLayout.isEnabled = state
        binding.loginLayout.isEnabled = state
        if(state) {
            binding.editProfile.text = getString(R.string.confirm_edit)
        } else {
            binding.editProfile.text = getString(R.string.edit_profile)
        }
    }

    private fun observeViewModel() = with(viewModel) {
        userData.listenValue(::bindUserData)
        editState.listenValue(::bindEditState)
    }
}