package com.example.reminderapp.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
import com.example.reminderapp.common.extensions.onSuccess
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.TokenModel
import com.example.reminderapp.domain.usecases.LoginUseCase
import com.example.reminderapp.domain.usecases.RegistrationUseCase
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface LoginViewModel {
    val navigationFlow: Flow<Int?>
    val buttonEnabledState: Flow<Boolean>
    val loadingState: Flow<Boolean>
    fun openRegistrationClicked()
    fun updateLogin(login: String)
    fun updatePassword(password: String)
    fun loginClicked()
}

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository
): LoginViewModel, ViewModel() {

    private val loginData: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordData: MutableStateFlow<String> = MutableStateFlow("")
    private val loginState: MutableStateFlow<State<TokenModel>> = MutableStateFlow(State.Idle)
    override val navigationFlow: MutableStateFlow<Int?> = MutableStateFlow(null)

    override val loadingState: Flow<Boolean>
        get() = loginState.map { it.isLoading() }
    override val buttonEnabledState: Flow<Boolean>
        get() = combine(
            loginData,
            passwordData
        ) { login, password ->
            login.isNotBlank() && password.isNotBlank()
        }

    override fun updatePassword(password: String) {
        passwordData.update { password }
    }


    override fun updateLogin(login: String) {
        loginData.update { login }
    }


    override fun openRegistrationClicked() {
        navigationFlow.update { R.id.action_registerFragment_to_loginFragment }
    }

    override fun loginClicked() {
        loginUseCase(
            login = loginData.value,
            password = passwordData.value
        ).onEach {
            state ->
            loginState.emit(state)
            state.onSuccess {
                preferencesDataStoreRepository.updateUserToken(it.token)
            }
        }.launchIn(viewModelScope)
    }
}