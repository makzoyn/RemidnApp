package com.example.reminderapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemindDao {
    @Insert
    suspend fun saveRemind(remind: RemindEntity): Long

    @Query("SELECT * FROM reminds WHERE id = :id")
    suspend fun getRemindById(id: Int): RemindEntity?

    @Query("SELECT * FROM reminds")
    suspend fun getAllReminds(): List<RemindEntity>?

    @Query("SELECT * FROM reminds WHERE title LIKE '%' || :title || '%'")
    suspend fun searchByTitle(title: String): List<RemindEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllReminds(reminds: List<RemindEntity>): List<Long>
}