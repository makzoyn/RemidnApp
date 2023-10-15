package com.example.reminderapp.api

import com.example.reminderapp.database.RemindEntry
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ServerAPI {
    @GET("/reminds/fetch")
    suspend fun getReminds(
        @Header("Authorization") token: String,
        @Query("login") login: String
    ): List<RemindEntry>
}