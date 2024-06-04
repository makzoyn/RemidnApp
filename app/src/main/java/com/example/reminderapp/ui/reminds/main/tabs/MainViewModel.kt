package com.example.reminderapp.ui.reminds.main.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface MainViewModel {
    val tabSelection: Flow<Int>
    fun tabSelectionChanged(position: Int)
}

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val preferenceRepository: PreferencesDataStoreRepository
): MainViewModel, ViewModel() {
    override val tabSelection: MutableStateFlow<Int> = MutableStateFlow(runBlocking { preferenceRepository.getTab() })

    override fun tabSelectionChanged(position: Int) {
        viewModelScope.launch {
            preferenceRepository.saveTab(position)
        }
        tabSelection.update { position }
    }
}