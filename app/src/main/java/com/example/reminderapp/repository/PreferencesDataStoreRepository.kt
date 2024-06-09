package com.example.reminderapp.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val USER_PREFERENCE_FILE_NAME = "user_preferences"

private val Context.datastore by preferencesDataStore(USER_PREFERENCE_FILE_NAME)

interface PreferencesDataStoreRepository {
    val usedDataStore: Flow<String>
    val tokenStream: Flow<String>
    val firebaseTokenStream: Flow<String>
    suspend fun fetchFirebaseToken(): String
    suspend fun updateFirebaseToken(token: String)
    suspend fun updateUserToken(token: String)
    suspend fun getToken(): String
    suspend fun clearPreferences()
    suspend fun saveTab(position: Int)
    suspend fun getTab(): Int
    suspend fun getOnBoardingState(): Boolean
    suspend fun updateOnBoardingState(isOnBoarder: Boolean)
}

class PreferencesDataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : PreferencesDataStoreRepository {

    private val dataStore = context.datastore

    override val usedDataStore: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emptyPreferences()
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val token = preferences[USER_TOKEN] ?: ""
            token
        }

    override suspend fun getToken(): String =
        dataStore.data.first()[USER_TOKEN] ?: ""


    override val tokenStream: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_TOKEN] ?: ""
        }

    override val firebaseTokenStream: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[FIREBASE_TOKEN_KEY] ?: ""
        }

    override suspend fun fetchFirebaseToken(): String =
        dataStore.data.first()[FIREBASE_TOKEN_KEY] ?: ""

    override suspend fun updateFirebaseToken(token: String) {
        dataStore.edit { preferences ->
            preferences[FIREBASE_TOKEN_KEY] = token
        }
    }

    override suspend fun updateUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    override suspend fun updateOnBoardingState(isOnBoarder: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = isOnBoarder
        }
    }

    override suspend fun getOnBoardingState(): Boolean =
        dataStore.data.first()[ONBOARDING_KEY] ?: false


    private suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
        }
    }

    private suspend fun clearFCMToken() {
        dataStore.edit { preferences ->
            preferences.remove(FIREBASE_TOKEN_KEY)
        }
    }

    private suspend fun clearTab() {
        dataStore.edit { preferences ->
            preferences.remove(TAB_KEY)
        }
    }

    private suspend fun clearOnBoardingState() {
        dataStore.edit { preferences ->
            preferences.remove(ONBOARDING_KEY)
        }
    }


    override suspend fun clearPreferences() {
        clearToken()
        clearFCMToken()
        clearTab()
        clearOnBoardingState()
    }

    override suspend fun saveTab(position: Int) {
        dataStore.edit { preferences ->
            preferences[TAB_KEY] = position.toString()
        }
    }

    override suspend fun getTab(): Int {
        return dataStore.data.first()[TAB_KEY]?.toIntOrNull() ?: 0
    }

    companion object PreferencesKeys {
        private val USER_TOKEN = stringPreferencesKey("USER_TOKEN")
        private val FIREBASE_TOKEN_KEY = stringPreferencesKey("FIREBASE_TOKEN_KEY")
        private val TAB_KEY = stringPreferencesKey("TAB_KEY")
        private val ONBOARDING_KEY = booleanPreferencesKey("ONBOARDING_KEY")
    }
}