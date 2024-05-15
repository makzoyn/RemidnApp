package com.example.reminderapp.ui.auth.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
import com.example.reminderapp.common.extensions.onSuccess
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.TokenModel
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

interface RegistrationViewModel {
    val buttonEnabledState: Flow<Boolean>
    val loadingState: Flow<Boolean>
    val navigationFlow: Flow<Int?>
    fun openLoginClicked()
    fun updateLogin(login: String)
    fun updatePassword(password: String)
    fun updateRepeatPassword(password: String)
    fun updateEmail(email: String)
    fun registrationClicked()
}

@HiltViewModel
class RegistrationViewModelImpl @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository
) : RegistrationViewModel, ViewModel() {

    private val loginData: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordData: MutableStateFlow<String> = MutableStateFlow("")
    private val repeatPasswordData: MutableStateFlow<String> = MutableStateFlow("")
    private val emailData: MutableStateFlow<String> = MutableStateFlow("")
    private val registrationState: MutableStateFlow<State<TokenModel>> =
        MutableStateFlow(State.Idle)
    override val navigationFlow: MutableStateFlow<Int?> = MutableStateFlow(null)

    override val loadingState: Flow<Boolean>
        get() = registrationState.map { it.isLoading() }
    override val buttonEnabledState: Flow<Boolean>
        get() = combine(
            loginData,
            passwordData,
            repeatPasswordData,
            emailData
        ) { login, password, repeatPassword, email ->
            login.isNotBlank() && email.isNotBlank() && (password.isNotBlank() && repeatPassword.isNotBlank() && (password == repeatPassword))
        }

    override fun openLoginClicked() {
        navigationFlow.update { R.id.action_registerFragment_to_loginFragment }
    }
    override fun updatePassword(password: String) {
        passwordData.update { password }
    }

    override fun updateRepeatPassword(password: String) {
        repeatPasswordData.update { password }
    }


    override fun updateLogin(login: String) {
        loginData.update { login }
    }

    override fun updateEmail(email: String) {
        emailData.update { email }
    }


    override fun registrationClicked() {
        registrationUseCase(
            login = loginData.value,
            password = passwordData.value,
            email = emailData.value
        ).onEach { state ->
            registrationState.emit(state)
            state.onSuccess {
                preferencesDataStoreRepository.updateUserToken(it.token)
            }
        }.launchIn(viewModelScope)
    }


}