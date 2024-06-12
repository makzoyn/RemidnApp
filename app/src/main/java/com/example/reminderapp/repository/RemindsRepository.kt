package com.example.reminderapp.repository

import com.example.reminderapp.api.RemindsApi
import com.example.reminderapp.api.model.requests.CreateRemindRequest
import com.example.reminderapp.api.model.requests.DeleteRemindsRequest
import com.example.reminderapp.api.model.requests.UpdateRemindRequest
import com.example.reminderapp.api.model.responses.mapToDao
import com.example.reminderapp.api.model.responses.mapToDomain
import com.example.reminderapp.common.extensions.mapResult
import com.example.reminderapp.common.networkresult.NetworkResult
import com.example.reminderapp.database.RemindDao
import com.example.reminderapp.database.mapToDomain
import com.example.reminderapp.database.mapToDomainList
import com.example.reminderapp.domain.model.RemindModel
import com.example.reminderapp.domain.model.RemindsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface RemindsRepository {
    suspend fun getAllReminds(): NetworkResult<RemindsModel>
    suspend fun getAllRemindsByRoom(): List<RemindModel>?
    suspend fun getAllNotesByRoom(): List<RemindModel>?
    suspend fun getAllNotes(): NetworkResult<RemindsModel>
    suspend fun getRemindsByTitle(
        title: String
    ): NetworkResult<RemindsModel>

    suspend fun getRemindsByTitleByRoom(
        title: String
    ): List<RemindModel>?

    suspend fun getNotesByTitleByRoom(
        title: String
    ): List<RemindModel>?

    suspend fun getNotesByTitle(
        title: String
    ): NetworkResult<RemindsModel>

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

    suspend fun getRemindById(
        id: Int
    ): NetworkResult<RemindModel>

    suspend fun getRemindByIdFromRoom(
        id: Int
    ): RemindModel?
}

class RemindsRepositoryImpl(
    private val api: RemindsApi,
    private val remindDao: RemindDao
) : RemindsRepository {
    override suspend fun getAllReminds(): NetworkResult<RemindsModel> {
        val result = api.getAllReminds()
        result.mapResult {
            CoroutineScope(Dispatchers.IO).launch {
                remindDao.saveAllReminds(it.mapToDao())
            }
        }
        return result.mapResult { it.mapToDomain() }
    }

    override suspend fun getAllRemindsByRoom(): List<RemindModel>? {
        return remindDao.getAllReminds()
            ?.filter { it.needToNotified }
            ?.mapToDomainList()
    }

    override suspend fun getRemindsByTitleByRoom(title: String): List<RemindModel>? {
        return remindDao.searchByTitle(title)
            ?.filter { it.needToNotified }
            ?.mapToDomainList()
    }

    override suspend fun getNotesByTitleByRoom(title: String): List<RemindModel>? {
        return remindDao.searchByTitle(title)
            ?.filter { it.needToNotified.not() }
            ?.mapToDomainList()
    }

    override suspend fun getAllNotesByRoom(): List<RemindModel>? {
        return remindDao.getAllReminds()
            ?.filter { it.needToNotified.not() }
            ?.mapToDomainList()
    }

    override suspend fun getRemindByIdFromRoom(id: Int): RemindModel? {
        return remindDao.getRemindById(id)
            ?.mapToDomain()
    }

    override suspend fun getAllNotes(): NetworkResult<RemindsModel> {
        val result = api.getAllNotes()
        result.mapResult {
            CoroutineScope(Dispatchers.IO).launch {
                remindDao.saveAllReminds(it.mapToDao())
            }
        }
        return result.mapResult { it.mapToDomain() }
    }

    override suspend fun getRemindsByTitle(title: String): NetworkResult<RemindsModel> =
        api.getRemindsByTitle(title).mapResult { it.mapToDomain() }

    override suspend fun getNotesByTitle(title: String): NetworkResult<RemindsModel> =
        api.getNotesByTitle(title).mapResult { it.mapToDomain() }

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

    override suspend fun getRemindById(id: Int): NetworkResult<RemindModel> =
        api.getRemindById(id).mapResult { it.mapToDomain() }
}