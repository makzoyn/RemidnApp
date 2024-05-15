package com.example.reminderapp.di

import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.example.reminderapp.repository.PreferencesDataStoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {
    @Singleton
    @Binds
    fun bindDataStore(impl: PreferencesDataStoreRepositoryImpl) : PreferencesDataStoreRepository
}