package com.example.reminderapp.di

import com.example.reminderapp.api.AuthApi
import com.example.reminderapp.api.RemindsApi
import com.example.reminderapp.api.UserApi
import com.example.reminderapp.repository.AuthRepository
import com.example.reminderapp.repository.AuthRepositoryImpl
import com.example.reminderapp.repository.RemindsRepository
import com.example.reminderapp.repository.RemindsRepositoryImpl
import com.example.reminderapp.repository.UserRepository
import com.example.reminderapp.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    internal fun provideAuthRepository(
        api: AuthApi
    ): AuthRepository = AuthRepositoryImpl(api)

    @Singleton
    @Provides
    internal fun provideRemindsRepository(
        api: RemindsApi
    ): RemindsRepository = RemindsRepositoryImpl(api)

    @Singleton
    @Provides
    internal fun provideUserRepository(
        api: UserApi
    ): UserRepository = UserRepositoryImpl(api)
}