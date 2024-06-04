package com.example.reminderapp.ui.reminds.main.tabs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.R
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
    val navigationFlow: Flow<Pair<Int?, Any?>?>

    fun tabSelectionChanged(position: Int)
    fun profile()
}

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val preferenceRepository: PreferencesDataStoreRepository
): MainViewModel, ViewModel() {
    override val tabSelection: MutableStateFlow<Int> = MutableStateFlow(runBlocking { preferenceRepository.getTab() })
    override val navigationFlow: MutableStateFlow<Pair<Int?, Any?>?> = MutableStateFlow(null)

    override fun tabSelectionChanged(position: Int) {
        viewModelScope.launch {
            preferenceRepository.saveTab(position)
        }
        tabSelection.update { position }
    }

    override fun profile() {
        navigationFlow.update { Pair(R.id.action_mainFragment_to_profileFragment, null) }
    }
}