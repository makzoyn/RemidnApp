package com.example.reminderapp.domain.usecases

import com.example.reminderapp.common.extensions.mapToState
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.UserModel
import com.example.reminderapp.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<State<UserModel>> = flow {
        emit(State.Loading)
        val result = repository.getUser()
        emit(result.mapToState())
    }
}