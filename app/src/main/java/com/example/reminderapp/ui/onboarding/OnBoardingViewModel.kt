package com.example.reminderapp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
import com.example.reminderapp.domain.model.OnBoardingItem
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface OnBoardingViewModel {
    fun nextScreen()
    fun finishOnBoarding()
    val buttonState: Flow<Pair<Boolean, String>>
    val navigationFlow: Flow<Int?>
}

@HiltViewModel
class OnBoardingViewModelImpl @Inject constructor(
    private val onBoardingDataHolder: OnBoardingDataHolder,
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository
) : ViewModel(), OnBoardingViewModel {
    override val navigationFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val buttonState: MutableStateFlow<Pair<Boolean, String>> =
        MutableStateFlow(Pair(false, "Далее"))

    override fun finishOnBoarding() {
        viewModelScope.launch {
            preferencesDataStoreRepository.updateOnBoardingState(true)
        }
        navigationFlow.update { R.id.action_onBoarding_to_main }
    }

    init {
        initScreenData()
    }

    private fun initScreenData() {
        onBoardingDataHolder.updateData(
            OnBoardingItem(
                id = 0,
                imageId = R.drawable.first_onboarding,
                text = "Вы можете добавлять и создавать задачи"
            )
        )
    }

    override fun nextScreen() {
        onBoardingDataHolder.updateData(
            OnBoardingItem(
                id = 1,
                imageId = R.drawable.second_onboarding,
                text = "Вы можете добавлять и создавать задачи"
            )
        )
        buttonState.update { Pair(true, "Включить уведомления") }
    }
}