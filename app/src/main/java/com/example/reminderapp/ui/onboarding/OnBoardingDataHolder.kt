package com.example.reminderapp.ui.onboarding

import com.example.reminderapp.domain.model.OnBoardingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface OnBoardingDataHolder{
    val data: Flow<OnBoardingItem?>
    fun updateData(newData: OnBoardingItem)
}
class OnBoardingDataHolderImpl @Inject constructor(): OnBoardingDataHolder {
    override val data: MutableStateFlow<OnBoardingItem?> = MutableStateFlow(null)
    override fun updateData(newData: OnBoardingItem) {
        data.update { newData }
    }
}