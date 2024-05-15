package com.example.reminderapp.di

import com.example.reminderapp.api.AuthApi
import com.example.reminderapp.api.RemindsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    internal fun provideAuthApi(
        retrofit: Retrofit
    ): AuthApi =retrofit.create(AuthApi::class.java)

    @Singleton
    @Provides
    internal fun provideRemindsApi(
        retrofit: Retrofit
    ): RemindsApi = retrofit.create(RemindsApi::class.java)

}