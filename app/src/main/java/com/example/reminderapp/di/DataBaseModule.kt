package com.example.reminderapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideAppDatabase(@ApplicationContext appContext: Context): RemindDatabase {
//        return Room.databaseBuilder(
//            appContext,
//            RemindDatabase::class.java,
//            "reminder_database"
//        ).build()
//    }
}