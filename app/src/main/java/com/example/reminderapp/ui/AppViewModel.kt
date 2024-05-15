package com.example.reminderapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
import com.example.reminderapp.common.extensions.onSuccess
import com.example.reminderapp.domain.usecases.DecideFcmDeviceRegistrationUseCase
import com.example.reminderapp.domain.usecases.DeleteRemindAfterPushUseCase
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.example.reminderapp.singleresult.ReceiveMessageFromPushEvent
import com.example.reminderapp.singleresult.ReceiveMessageFromPushResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AppViewModel {
    val navigationFlow: Flow<Int?>
    fun deleteRemindAfterPush(id: Int)
}

@HiltViewModel
class AppViewModelImpl @Inject constructor(
    private val preferencesRepository: PreferencesDataStoreRepository,
    private val decideFcmDeviceRegistration: DecideFcmDeviceRegistrationUseCase,
    private val deleteRemindAfterPushUseCase: DeleteRemindAfterPushUseCase,
    private val receiveMessageFromPushResult: ReceiveMessageFromPushResult
) : AppViewModel, ViewModel() {

    override val navigationFlow: MutableStateFlow<Int?> = MutableStateFlow(null)

    init {
        subscribeOnToken()
        viewModelScope.launch {
            decideFcmDeviceRegistration()
        }
    }

    private fun subscribeOnToken() {
        preferencesRepository.tokenStream
            .distinctUntilChanged()
            .onEach {
                if (it.isNotEmpty()) {
                    navigationFlow.emit(R.id.mainRemindsFragment)
                } else {
                    navigationFlow.emit(R.id.registerFragment)
                }
            }.launchIn(viewModelScope)
    }

    override fun deleteRemindAfterPush(id: Int) {
        deleteRemindAfterPushUseCase(id)
            .onEach { state ->
                state.onSuccess {
                    receiveMessageFromPushResult.postEvent(
                        ReceiveMessageFromPushEvent.RemindReceived
                    )
                }
            }.launchIn(viewModelScope)
    }
}