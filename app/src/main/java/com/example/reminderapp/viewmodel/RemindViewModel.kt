package com.example.reminderapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.database.RemindDatabase
import com.example.reminderapp.database.RemindEntry
import com.example.reminderapp.repository.RemindRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemindViewModel(application: Application) : AndroidViewModel(application) {
    private val remindDao = RemindDatabase.getDatabase(application).remindDao()
    private val repository: RemindRepository = RemindRepository(remindDao)

    val getAllReminds: LiveData<List<RemindEntry>> = repository.getAllReminds()
    val getLastRemindId : LiveData<Int> = repository.getLastRemindId()
    fun insert(remindEntry: RemindEntry){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(remindEntry)
        }
    }

    suspend fun getRemindsFromServer(token: String, login: String) {
        repository.getRemindsFromServer(token, login)
    }

    fun update(remindEntry: RemindEntry){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(remindEntry)
        }
    }

    fun delete(remindEntry: RemindEntry){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(remindEntry)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}