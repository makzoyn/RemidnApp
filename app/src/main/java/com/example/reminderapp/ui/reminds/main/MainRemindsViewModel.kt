package com.example.reminderapp.ui.reminds.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
import com.example.reminderapp.common.extensions.onSuccess
import com.example.reminderapp.common.formatters.DateTimeFormatter
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.RemindsModel
import com.example.reminderapp.domain.model.mapToItems
import com.example.reminderapp.domain.usecases.DeleteRemindUseCase
import com.example.reminderapp.domain.usecases.GetAllRemindsUseCase
import com.example.reminderapp.singleresult.ReceiveMessageFromPushEvent
import com.example.reminderapp.singleresult.ReceiveMessageFromPushResult
import com.example.reminderapp.ui.reminds.adapter.model.RemindItem
import com.example.reminderapp.ui.reminds.edit.EditRemindFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MainRemindsViewModel {
    val loadingState: Flow<Boolean>
    val remindsData: Flow<List<RemindItem>?>
    val navigationFlow: Flow<Pair<Int?, Any?>?>
    val deleteVisibilityState: Flow<Boolean>
    fun selectedChanged(id: Int)
    fun editRemind(id: Int)
    fun unselectAll()
    fun refreshReminds()
    fun createRemindClicked()
    fun deleteSelectedReminds()
}

@HiltViewModel
class MainRemindsViewModelImpl @Inject constructor(
    private val getAllRemindsUseCase: GetAllRemindsUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
    private val receiveMessageFromPushResult: ReceiveMessageFromPushResult,
    private val deleteRemindUseCase: DeleteRemindUseCase
) : MainRemindsViewModel, ViewModel() {


    override val remindsData: MutableStateFlow<List<RemindItem>?> = MutableStateFlow(null)
    private val remindsState: MutableStateFlow<State<RemindsModel>> = MutableStateFlow(State.Idle)
    override val navigationFlow: MutableStateFlow<Pair<Int?, Any?>?> = MutableStateFlow(null)
    private val deleteState: MutableStateFlow<State<Unit>> = MutableStateFlow(State.Idle)
    override val loadingState: Flow<Boolean>
        get() = remindsState.map { it.isLoading() }
    override val deleteVisibilityState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        getAllReminds()
        viewModelScope.launch {
            subscribeOnRemindDeleteFromPushResult()
        }
    }

    override fun deleteSelectedReminds() {
        remindsData.value?.filter { remind ->
                remind.isSelected
            }?.map { it.id }?.let {
            deleteRemindUseCase(it)
                .onEach { state ->
                    deleteState.emit(state)
                    state.onSuccess {
                        getAllReminds()
                    }
                }.launchIn(viewModelScope)
        }
    }

    override fun editRemind(id: Int) {
        navigationFlow.update { Pair(R.id.action_mainRemindsFragment_to_editRemindFragment, EditRemindFragment.Param(id)) }
    }

    override fun unselectAll() {
        deleteVisibilityState.update { false }
        remindsData.update { reminds ->
            reminds?.map { remind ->
                remind.copy(isSelected = false)
            }
        }
    }

    override fun selectedChanged(id: Int) {
        deleteVisibilityState.update { true }
        remindsData.update { reminds ->
            reminds?.map { remind ->
                if (remind.id == id) {
                    remind.copy(isSelected = remind.isSelected.not())
                } else
                    remind.copy()
            }
        }
    }

    private suspend fun subscribeOnRemindDeleteFromPushResult() {
        receiveMessageFromPushResult.events.collect { event ->
            if (event is ReceiveMessageFromPushEvent.RemindReceived) {
                refreshReminds()
            }
        }
    }

    override fun refreshReminds() {
        getAllReminds()
    }

    override fun createRemindClicked() {
        navigationFlow.update { Pair(R.id.action_mainRemindsFragment_to_createRemindFragment, null) }
    }

    private fun getAllReminds() {
        getAllRemindsUseCase()
            .onEach { state ->
                remindsState.emit(state)
                state.onSuccess { remindsModel ->
                    val sortedModel = remindsModel.reminds
                        .sortedBy { it.date }
                        .sortedBy { it.isNotified }
                    remindsData.emit(sortedModel.mapToItems(dateTimeFormatter))
                }
            }.launchIn(viewModelScope)
    }

}