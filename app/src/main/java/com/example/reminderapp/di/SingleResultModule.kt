package com.example.reminderapp.di

import com.example.reminderapp.singleresult.LogoutResult
import com.example.reminderapp.singleresult.NetworkErrorResult
import com.example.reminderapp.singleresult.ReceiveMessageFromPushResult
import com.example.reminderapp.singleresult.SearchResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingleResultModule {
    @Provides
    @Singleton
    fun provideDeleteMessageResult(): ReceiveMessageFromPushResult = ReceiveMessageFromPushResult()

    @Provides
    @Singleton
    fun provideNetworkErrorResult(): NetworkErrorResult = NetworkErrorResult()

    @Provides
    @Singleton
    fun provideLogoutResult(): LogoutResult = LogoutResult()

    @Provides
    @Singleton
    fun provideSearchResult(): SearchResult = SearchResult()
}