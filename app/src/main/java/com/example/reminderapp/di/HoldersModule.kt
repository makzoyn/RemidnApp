package com.example.reminderapp.di

import com.example.reminderapp.ui.onboarding.OnBoardingDataHolder
import com.example.reminderapp.ui.onboarding.OnBoardingDataHolderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HoldersModule {
    @Provides
    @Singleton
    internal fun provideHolder(): OnBoardingDataHolder = OnBoardingDataHolderImpl()
}