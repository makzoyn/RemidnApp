package com.example.reminderapp.repository

import com.example.reminderapp.api.RemindsApi
import com.example.reminderapp.api.model.requests.CreateRemindRequest
import com.example.reminderapp.api.model.requests.DeleteRemindsRequest
import com.example.reminderapp.api.model.requests.UpdateRemindRequest
import com.example.reminderapp.api.model.responses.mapToDomain
import com.example.reminderapp.common.extensions.mapResult
import com.example.reminderapp.common.networkresult.NetworkResult
import com.example.reminderapp.domain.model.RemindModel
import com.example.reminderapp.domain.model.RemindsModel

interface RemindsRepository {
    suspend fun getAllReminds(): NetworkResult<RemindsModel>
    suspend fun getAllNotes(): NetworkResult<RemindsModel>
    suspend fun createRemind(
        createRemindRequest: CreateRemindRequest
    ): NetworkResult<RemindModel>

    suspend fun updateRemind(
        id: Int,
        updateRemindRequest: UpdateRemindRequest
    ): NetworkResult<RemindModel>

    suspend fun deleteReminds(
        deleteRemindsRequest: DeleteRemindsRequest
    ): NetworkResult<Unit>

    suspend fun getRemindId(
        id: Int
    ): NetworkResult<RemindModel>
}

class RemindsRepositoryImpl(
    private val api: RemindsApi
) : RemindsRepository {
    override suspend fun getAllReminds(): NetworkResult<RemindsModel> =
        api.getAllReminds().mapResult { it.mapToDomain() }

    override suspend fun getAllNotes(): NetworkResult<RemindsModel> =
        api.getAllNotes().mapResult { it.mapToDomain() }

    override suspend fun createRemind(
        createRemindRequest: CreateRemindRequest
    ): NetworkResult<RemindModel> =
        api.createRemind(createRemindRequest).mapResult { it.mapToDomain() }

    override suspend fun updateRemind(
        id: Int,
        updateRemindRequest: UpdateRemindRequest
    ): NetworkResult<RemindModel> =
        api.updateRemind(id, updateRemindRequest).mapResult { it.mapToDomain() }

    override suspend fun deleteReminds(
        deleteRemindsRequest: DeleteRemindsRequest
    ): NetworkResult<Unit> = api.deleteReminds(deleteRemindsRequest)

    override suspend fun getRemindId(id: Int): NetworkResult<RemindModel> =
        api.getRemindById(id).mapResult { it.mapToDomain() }
}