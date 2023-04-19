package com.example.reminderapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = arrayOf(RemindEntry::class), version = 1, exportSchema = false)
abstract class RemindDatabase : RoomDatabase() {
    abstract fun remindDao() : RemindDao

    companion object {
        @Volatile
        private var INSTANCE: RemindDatabase? = null

        fun getDatabase(context: Context): RemindDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RemindDatabase::class.java,
                        "reminder_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}