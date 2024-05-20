package com.example.reminderapp.ui.auth.registration

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.reminderapp.R
import com.example.reminderapp.common.base.BaseFragment
import com.example.reminderapp.common.extensions.listenValue
import com.example.reminderapp.common.extensions.parentToolbar
import com.example.reminderapp.common.extensions.toggleAvailability
import com.example.reminderapp.common.extensions.toggleVisability
import com.example.reminderapp.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : BaseFragment(R.layout.fragment_registration) {
    private val viewModel: RegistrationViewModel by viewModels<RegistrationViewModelImpl>()
    private val binding by viewBinding(FragmentRegistrationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        bindUi()
    }

    private fun bindUi() {
        parentToolbar { isVisible = false }
        bindLoginInput()
        bindPasswordInput()
        bindEmailInput()
        bindRepeatPasswordInput()
        binding.registrationBtn.setOnClickListener {
            viewModel.registrationClicked()
        }
        binding.loginBtn.setOnClickListener {
            viewModel.openLoginClicked()
        }
    }

    private fun bindLoginInput() = with(binding.login) {
        addTextChangedListener {
            viewModel.updateLogin(it.toString())
        }
    }

    private fun bindPasswordInput() = with(binding.password) {
        addTextChangedListener {
            viewModel.updatePassword(it.toString())
        }
    }

    private fun bindRepeatPasswordInput() = with(binding.repeatPassword) {
        addTextChangedListener {
            viewModel.updatePassword(it.toString())
        }
    }

    private fun bindEmailInput() = with(binding.email) {
        addTextChangedListener {
            viewModel.updateEmail(it.toString())
        }
    }

    private fun observeViewModel() = with(viewModel) {
        buttonEnabledState.listenValue(binding.registrationBtn::toggleAvailability)
        loadingState.listenValue(binding.progressLayout::toggleVisability)
        navigationFlow.listenValue(::onNavigate)
    }


    private fun onNavigate(navId: Int?) {
        if(navId != null) {
            navigate(navId)
        }
    }

}