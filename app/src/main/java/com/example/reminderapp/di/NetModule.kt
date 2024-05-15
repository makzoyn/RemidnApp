package com.example.reminderapp.di


import android.util.Log
import com.example.reminderapp.BuildConfig
import com.example.reminderapp.api.interceptors.TokenInterceptor
import com.example.reminderapp.common.networkresult.NetworkResultCallAdapterFactory
import com.example.reminderapp.repository.PreferencesDataStoreRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Provides
    @Singleton
    internal fun provideClient(
        userTokenInterceptor: TokenInterceptor
        ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(userTokenInterceptor)
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(
        client: OkHttpClient,
        networkResultCallAdapterFactory: NetworkResultCallAdapterFactory
    ): Retrofit {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(networkResultCallAdapterFactory)
            .build()
    }


    @Provides
    @Singleton
    internal fun provideRetrofitAdapterFactory() : NetworkResultCallAdapterFactory =
        NetworkResultCallAdapterFactory()

}