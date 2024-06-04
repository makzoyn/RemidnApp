package com.example.reminderapp.ui.auth.login

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
import com.example.reminderapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()
    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        bindUi()
    }

    private fun bindUi() {
        bindLoginInput()
        bindPasswordInput()
        binding.registrationBtn.setOnClickListener {
            viewModel.openRegistrationClicked()
        }
        binding.loginBtn.setOnClickListener {
            viewModel.loginClicked()
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


    private fun observeViewModel() = with(viewModel) {
        buttonEnabledState.listenValue(binding.loginBtn::toggleAvailability)
        loadingState.listenValue(binding.progressLayout::toggleVisability)
        navigationFlow.listenValue(::onNavigate)
    }

    private fun onNavigate(navId: Int?) {
        if(navId != null) {
            navigate(navId)
        }
    }

}