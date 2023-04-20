package com.example.reminderapp.repository

import androidx.lifecycle.LiveData
import com.example.reminderapp.database.RemindDao
import com.example.reminderapp.database.RemindEntry

class RemindRepository(val remindDao : RemindDao) {

    suspend fun insert(remindEntry: RemindEntry) = remindDao.insert(remindEntry)

    suspend fun updateData(remindEntry: RemindEntry) = remindDao.update(remindEntry)

    suspend fun deleteItem(remindEntry: RemindEntry) = remindDao.delete(remindEntry)

    suspend fun deleteAll() {
        remindDao.deleteAll()
    }
    fun getAllReminds() : LiveData<List<RemindEntry>> = remindDao.getAllReminds()
}