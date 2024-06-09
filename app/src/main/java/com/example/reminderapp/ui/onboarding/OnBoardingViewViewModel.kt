package com.example.reminderapp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.domain.model.OnBoardingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface OnBoardingViewViewModel {
    val screenData: Flow<OnBoardingItem?>
}

@HiltViewModel
class OnBoardingViewViewModelImpl @Inject constructor(
    private val onBoardingDataHolder: OnBoardingDataHolder
): OnBoardingViewViewModel, ViewModel() {
    override val screenData: MutableStateFlow<OnBoardingItem?> = MutableStateFlow(null)
    init {
        viewModelScope.launch {
            onBoardingDataHolder.data.collect { data ->
                screenData.emit(data)
            }
        }
    }
}