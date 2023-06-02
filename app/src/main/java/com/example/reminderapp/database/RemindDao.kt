package com.example.reminderapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RemindDao {

    @Insert
    suspend fun insert(remindEntry: RemindEntry)

    @Insert
    suspend fun insertAll(remindEntries: List<RemindEntry>)

    @Delete
    suspend fun delete(remindEntry: RemindEntry)

    @Update
    suspend fun update(remindEntry: RemindEntry)

    @Query("DELETE FROM reminds_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM reminds_table ORDER BY date ASC")
    fun getAllReminds() : LiveData<List<RemindEntry>>

    @Query("SELECT id FROM reminds_table ORDER BY id DESC LIMIT 1")
    fun getLastRemindId() : LiveData<Int>



}