package com.example.reminderapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RemindEntity::class], version = 1)
abstract class RemindDatabase: RoomDatabase() {
    abstract fun remindDao(): RemindDao
}