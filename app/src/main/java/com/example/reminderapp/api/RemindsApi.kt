package com.example.reminderapp.api

import com.example.reminderapp.api.model.requests.CreateRemindRequest
import com.example.reminderapp.api.model.requests.DeleteRemindsRequest
import com.example.reminderapp.api.model.requests.UpdateRemindRequest
import com.example.reminderapp.api.model.responses.RemindResponse
import com.example.reminderapp.api.model.responses.RemindsResponse
import com.example.reminderapp.common.networkresult.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RemindsApi {
    @GET("reminds/fetch")
    suspend fun getAllReminds(): NetworkResult<RemindsResponse>

    @GET("reminds/fetch/search")
    suspend fun getRemindsByTitle(
        @Query("title") title: String
    ): NetworkResult<RemindsResponse>
    @GET("reminds/notes/search")
    suspend fun getNotesByTitle(
        @Query("title") title: String
    ): NetworkResult<RemindsResponse>

    @GET("reminds/notes")
    suspend fun getAllNotes(): NetworkResult<RemindsResponse>

    @POST("reminds/create")
    suspend fun createRemind(
        @Body createRemindRequest: CreateRemindRequest
    ): NetworkResult<RemindResponse>

    @PATCH("reminds/update/{id}")
    suspend fun updateRemind(
        @Path("id") id: Int,
        @Body updateRemindRequest: UpdateRemindRequest
    ): NetworkResult<RemindResponse>

    @POST("reminds/delete")
    suspend fun deleteReminds(
        @Body deleteRemindsRequest: DeleteRemindsRequest
    ): NetworkResult<Unit>

    @GET("reminds/fetch/{id}")
    suspend fun getRemindById(
        @Path("id") id: Int
    ): NetworkResult<RemindResponse>
}