package com.example.reminderapp.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.reminderapp.R
import com.example.reminderapp.alarm.AlarmBroadcast
import com.example.reminderapp.api.ServerAPI
import com.example.reminderapp.database.RemindDao
import com.example.reminderapp.database.RemindEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class RemindRepository(val remindDao: RemindDao) {

    private val serverAPI: ServerAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080") // Замените на базовый URL вашего сервера
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val originalRequest = chain.request()
                        val token = "bd6e5304-dc9f-4164-a02a-58e667fc7999" // Замените на вашу логику получения токена авторизации
                        val newRequest = originalRequest.newBuilder()
                            .header("Bearer-Authorization", token)
                            .build()
                        chain.proceed(newRequest)
                    }
                    .build()
            ).build()

        serverAPI = retrofit.create(ServerAPI::class.java)
    }

    private suspend fun saveRemindsToLocalDatabase(reminds: List<RemindEntry>) {
        withContext(Dispatchers.IO) {
            val localReminds = remindDao.getAllReminds().value // Get all local reminds

            val remindsToAdd = mutableListOf<RemindEntry>()
            val remindsToUpdate = mutableListOf<RemindEntry>()

            for (serverRemind in reminds) {
                val localRemind = localReminds?.find { it.id == serverRemind.id }
                if (localRemind != null) {
                    if (localRemind != serverRemind) {
                        remindsToUpdate.add(serverRemind)
                    }
                } else {
                    remindsToAdd.add(serverRemind)
                }
            }

            remindDao.deleteAll()

            val mergedReminds = remindsToUpdate + remindsToAdd
            remindDao.insertAll(mergedReminds)
        }
    }

    suspend fun getRemindsFromServer(token: String, login: String) {
        try {
            val reminds = serverAPI.getReminds(token, login) // список напоминаний с сервера
            saveRemindsToLocalDatabase(reminds)
        } catch (e: Exception) {
            Log.e("TAG", "Ошибка при выполнении запроса к серверу: ${e.message}")
        }
    }

    suspend fun insert(remindEntry: RemindEntry) = remindDao.insert(remindEntry)

    suspend fun updateData(remindEntry: RemindEntry) = remindDao.update(remindEntry)

    suspend fun deleteItem(remindEntry: RemindEntry) = remindDao.delete(remindEntry)

    suspend fun deleteAll() {
        remindDao.deleteAll()
    }

    fun getAllReminds(): LiveData<List<RemindEntry>> = remindDao.getAllReminds()


    fun getLastRemindId(): LiveData<Int> = remindDao.getLastRemindId()

}