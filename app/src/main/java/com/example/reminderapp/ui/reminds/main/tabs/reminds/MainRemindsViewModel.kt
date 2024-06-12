package com.example.reminderapp.ui.reminds.main.tabs.reminds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
import com.example.reminderapp.common.extensions.onError
import com.example.reminderapp.common.extensions.onSuccess
import com.example.reminderapp.common.formatters.DateTimeFormatter
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.RemindsModel
import com.example.reminderapp.domain.model.mapToItems
import com.example.reminderapp.domain.usecases.DeleteRemindUseCase
import com.example.reminderapp.domain.usecases.GetAllRemindsUseCase
import com.example.reminderapp.domain.usecases.GetRemindsByTitleUseCase
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.example.reminderapp.repository.RemindsRepository
import com.example.reminderapp.singleresult.NetworkErrorEvents
import com.example.reminderapp.singleresult.NetworkErrorResult
import com.example.reminderapp.singleresult.ReceiveMessageFromPushEvent
import com.example.reminderapp.singleresult.ReceiveMessageFromPushResult
import com.example.reminderapp.singleresult.SearchEvents
import com.example.reminderapp.singleresult.SearchResult
import com.example.reminderapp.ui.reminds.adapter.model.RemindItem
import com.example.reminderapp.ui.reminds.edit.EditRemindFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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
    val internetConnectionState: Flow<Boolean>
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
    private val deleteRemindUseCase: DeleteRemindUseCase,
    private val networkErrorResult: NetworkErrorResult,
    private val searchResult: SearchResult,
    private val getRemindsByTitleUseCase: GetRemindsByTitleUseCase,
    private val preferencesRepository: PreferencesDataStoreRepository,
    private val remindsRepository: RemindsRepository
) : MainRemindsViewModel, ViewModel() {

    override val internetConnectionState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val remindsData: MutableStateFlow<List<RemindItem>?> = MutableStateFlow(null)
    private val remindsState: MutableStateFlow<State<RemindsModel>> = MutableStateFlow(State.Idle)
    override val navigationFlow: MutableStateFlow<Pair<Int?, Any?>?> = MutableStateFlow(null)
    private val deleteState: MutableStateFlow<State<Unit>> = MutableStateFlow(State.Idle)
    override val loadingState: Flow<Boolean>
        get() = remindsState.map { it.isLoading() }
    override val deleteVisibilityState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        getAllReminds()
        subscribeOnRemindDeleteFromPushResult()
        subscribeOnSearchResult()
        subscribeOnInternetConnectionStream()
    }

    private fun subscribeOnInternetConnectionStream() {
        preferencesRepository.internetConnectionStream
            .distinctUntilChanged()
            .onEach {
                internetConnectionState.emit(it)
            }.launchIn(viewModelScope)
    }

    private fun subscribeOnSearchResult() {
        viewModelScope.launch {
            searchResult.events.collect { event ->
                if (event is SearchEvents.QueryChanged) {
                    getRemindsByTitle(event.text)
                }
            }
        }
    }

    private fun getRemindsByTitle(title: String) {
        if(internetConnectionState.value) {
            getRemindsByTitleUseCase(title)
                .onEach { state ->
                    remindsState.emit(state)
                    state.onSuccess { remindsModel ->
                        val sortedModel = remindsModel.reminds
                            .sortedBy { it.date }
                            .sortedBy { it.isNotified }
                        remindsData.emit(sortedModel.mapToItems(dateTimeFormatter))
                    }
                    state.onError { error ->
                        networkErrorResult.postEvent(
                            NetworkErrorEvents.ShowErrorDialog(
                                "Ошибка",
                                error.errorMessage
                            )
                        )
                    }
                }.launchIn(viewModelScope)
        } else {
            viewModelScope.launch {
                searchRemindsByTitleFromRoom(title)
            }
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
        navigationFlow.update {
            Pair(
                R.id.action_mainFragment_to_editRemindFragment,
                EditRemindFragment.Param(id)
            )
        }
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

    private fun subscribeOnRemindDeleteFromPushResult() {
        viewModelScope.launch {
            receiveMessageFromPushResult.events.collect { event ->
                if (event is ReceiveMessageFromPushEvent.RemindReceived) {
                    refreshReminds()
                }
            }
        }
    }

    override fun refreshReminds() {
        getAllReminds()
    }

    override fun createRemindClicked() {
        navigationFlow.update { Pair(R.id.action_mainFragment_to_createRemindFragment, null) }
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
                    state.onError { error ->
                        getRemindsFromRoom()
                        networkErrorResult.postEvent(
                            NetworkErrorEvents.ShowErrorDialog(
                                title = "Ошибка",
                                message = error.errorMessage
                            )
                        )
                    }
                }.launchIn(viewModelScope)
        }

    private suspend fun getRemindsFromRoom() {
        val data = remindsRepository.getAllRemindsByRoom()
            ?.sortedBy { it.date }
            ?.sortedBy { it.isNotified }
        remindsData.emit(data?.mapToItems(dateTimeFormatter))
    }

    private suspend fun searchRemindsByTitleFromRoom(title: String) {
        val data = remindsRepository.getRemindsByTitleByRoom(title)
            ?.sortedBy { it.date }
            ?.sortedBy { it.isNotified }
        remindsData.emit(data?.mapToItems(dateTimeFormatter))
    }

}