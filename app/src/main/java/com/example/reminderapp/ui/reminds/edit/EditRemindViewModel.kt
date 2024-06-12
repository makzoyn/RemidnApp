package com.example.reminderapp.ui.reminds.edit

import android.text.SpannableStringBuilder
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
import com.example.reminderapp.common.extensions.onError
import com.example.reminderapp.common.extensions.onSuccess
import com.example.reminderapp.common.extensions.requireParam
import com.example.reminderapp.common.formatters.DateTimeFormatter
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.RemindModel
import com.example.reminderapp.domain.usecases.GetRemindUseCase
import com.example.reminderapp.domain.usecases.UpdateRemindUseCase
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.example.reminderapp.repository.RemindsRepository
import com.example.reminderapp.singleresult.NetworkErrorEvents
import com.example.reminderapp.singleresult.NetworkErrorResult
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

interface EditRemindViewModel {
    val checkBoxState: Flow<Boolean>
    val uiTimeData: Flow<String>
    val uiDateData: Flow<String>
    val navigationFlow: Flow<Int?>
    val loadingState: Flow<Boolean>
    val titleDataFirst: Flow<String>
    val descriptionDataFirst: Flow<String?>
    val internetConnectionState: Flow<Boolean>
    fun updateRemindClicked()
    fun titleChanged(title: String)
    fun descriptionChanged(description: String)
    fun checkBoxChanged(isChecked: Boolean)
    fun remindTimeChanged(hour: Int, minute: Int)
    fun remindDateChanged(date: String)

}

@HiltViewModel
class EditRemindViewModelImpl @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter,
    private val getRemindUseCase: GetRemindUseCase,
    private val updateRemindUseCase: UpdateRemindUseCase,
    private val preferencesRepository: PreferencesDataStoreRepository,
    private val remindsRepository: RemindsRepository,
    private val networkErrorResult: NetworkErrorResult,
    savedStateHandle: SavedStateHandle
) : EditRemindViewModel, ViewModel() {
    override val checkBoxState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val titleData: MutableStateFlow<String> = MutableStateFlow("")
    override val titleDataFirst: MutableStateFlow<String> = MutableStateFlow("")
    private val descriptionData: MutableStateFlow<String?> = MutableStateFlow(null)
    override val descriptionDataFirst: MutableStateFlow<String?> = MutableStateFlow(null)
    private val timeData: MutableStateFlow<String?> = MutableStateFlow(null)
    private val dateData: MutableStateFlow<String?> = MutableStateFlow(null)
    override val uiTimeData: MutableStateFlow<String> = MutableStateFlow("")
    override val uiDateData: MutableStateFlow<String> = MutableStateFlow("")
    override val navigationFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val createRemindState: MutableStateFlow<State<RemindModel>> =
        MutableStateFlow(State.Idle)
    private val getRemindState: MutableStateFlow<State<RemindModel>> = MutableStateFlow(State.Idle)
    override val internetConnectionState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val loadingState: Flow<Boolean>
        get() = createRemindState.map { it.isLoading() }

    private val id = savedStateHandle.requireParam<EditRemindFragment.Param>().id

    init {
        getRemind()
        subscribeOnInternetConnectionStream()
    }

    private fun subscribeOnInternetConnectionStream() {
        preferencesRepository.internetConnectionStream
            .distinctUntilChanged()
            .onEach {
                internetConnectionState.emit(it)
            }.launchIn(viewModelScope)
    }

    private fun getRemind() {
        getRemindUseCase(id)
            .onEach { state ->
                getRemindState.emit(state)
                state.onSuccess { remindModel ->
                    timeData.update { remindModel.time }
                    dateData.update { remindModel.date }
                    titleDataFirst.update { remindModel.title }
                    descriptionDataFirst.update { remindModel.description }
                    uiTimeData.update {
                        remindModel.time?.let {
                            dateTimeFormatter.formatTime(
                                remindModel.time.take(2).toInt(),
                                remindModel.time.take(5).takeLast(2).toInt()
                            )
                        } ?: "Выберите время"
                    }
                    uiDateData.update { remindModel.date ?: "Выберите дату" }
                    checkBoxState.update { remindModel.time != null && remindModel.date != null }
                }
                state.onError { error ->
                    getRemindFromRoom()
                    networkErrorResult.postEvent(
                        NetworkErrorEvents.ShowErrorDialog(
                            title = "Ошибка",
                            message = error.errorMessage
                        )
                    )
                }
            }.launchIn(viewModelScope)
    }

    private suspend fun getRemindFromRoom() {
        remindsRepository.getRemindByIdFromRoom(id)?.let { remindModel ->
            timeData.update { remindModel.time }
            dateData.update { remindModel.date }
            titleDataFirst.update { remindModel.title }
            descriptionDataFirst.update { remindModel.description }
            uiTimeData.update {
                remindModel.time?.let {
                    dateTimeFormatter.formatTime(
                        remindModel.time.take(2).toInt(),
                        remindModel.time.take(5).takeLast(2).toInt()
                    )
                } ?: "Выберите время"
            }
            uiDateData.update { remindModel.date ?: "Выберите дату" }
            checkBoxState.update { remindModel.time != null && remindModel.date != null }
        }
    }
    override fun checkBoxChanged(isChecked: Boolean) {
        checkBoxState.update { isChecked }
    }

    override fun titleChanged(title: String) {
        titleData.update { title }
    }

    override fun descriptionChanged(description: String) {
        descriptionData.update { description }
    }

    override fun remindTimeChanged(hour: Int, minute: Int) {
        timeData.update {
            val hourSpan = SpannableStringBuilder()
            if (hour < 10) {
                hourSpan.append("0")
            }
            hourSpan.append("$hour:$minute:00")
            hourSpan.toString()
        }
        uiTimeData.update { dateTimeFormatter.formatTime(hour, minute) }
    }

    override fun remindDateChanged(date: String) {
        dateData.update { date }
        uiDateData.update { date }
    }

    override fun updateRemindClicked() {
        updateRemindUseCase(
            id = id,
            title = titleData.value,
            description = descriptionData.value,
            time = timeData.value,
            date = dateData.value,
            needToNotified = checkBoxState.value
        ).onEach { state ->
            createRemindState.emit(state)
            state.onSuccess {
                navigationFlow.emit(R.id.action_editRemindFragment_to_mainFragment)
            }
        }.launchIn(viewModelScope)
    }

}