package com.example.reminderapp.di

import com.example.reminderapp.common.formatters.DateTimeFormatter
import com.example.reminderapp.common.formatters.DateTimeFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UtilsModule {
    @Singleton
    @Binds
    fun bindDateFormatter(impl: DateTimeFormatterImpl): DateTimeFormatter
}