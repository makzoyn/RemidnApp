package com.example.reminderapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.common.extensions.onError
import com.example.reminderapp.common.extensions.onSuccess
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.UserModel
import com.example.reminderapp.domain.usecases.GetUserUseCase
import com.example.reminderapp.singleresult.LogoutEvents
import com.example.reminderapp.singleresult.LogoutResult
import com.example.reminderapp.singleresult.NetworkErrorEvents
import com.example.reminderapp.singleresult.NetworkErrorResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileViewModel {
    val userData: Flow<UserModel?>
    val loadingState: Flow<Boolean>
    val editState: Flow<Boolean>
    fun logOut()
    fun changeEditState()
}

@HiltViewModel
class ProfileViewModelImpl @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val networkErrorResult: NetworkErrorResult,
    private val logoutResult: LogoutResult
) : ViewModel(), ProfileViewModel {

    override val editState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val userData: MutableStateFlow<UserModel?> = MutableStateFlow(null)
    private val userDataState: MutableStateFlow<State<UserModel?>> = MutableStateFlow(State.Idle)
    override val loadingState: Flow<Boolean>
        get() = combine(
            userDataState
        ) { states ->
            states.any { it.isLoading() }
        }

    init {
        getUser()
    }

    override fun changeEditState() {
        editState.update { editState.value.not() }
    }

    override fun logOut() {
        viewModelScope.launch {
            logoutResult.postEvent(
                LogoutEvents.Logout
            )
        }
    }

    private fun getUser() {
        getUserUseCase()
            .onEach { state ->
                userDataState.emit(state)
                state.onSuccess { userModel ->
                    userData.emit(userModel)
                }
                state.onError { error ->
                    networkErrorResult.postEvent(
                        NetworkErrorEvents.ShowErrorDialog(
                            title = "Ошибка",
                            error.errorMessage
                        )
                    )
                }
            }.launchIn(viewModelScope)
    }
}