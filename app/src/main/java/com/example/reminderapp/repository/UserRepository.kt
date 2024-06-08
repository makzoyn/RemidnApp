package com.example.reminderapp.repository

import com.example.reminderapp.api.UserApi
import com.example.reminderapp.api.model.responses.toDomain
import com.example.reminderapp.common.extensions.mapResult
import com.example.reminderapp.common.networkresult.NetworkResult
import com.example.reminderapp.domain.model.UserModel

interface UserRepository {
    suspend fun getUser(): NetworkResult<UserModel>
}

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {
    override suspend fun getUser(): NetworkResult<UserModel> =
        api.getUser().mapResult {
            it.toDomain()
        }
}