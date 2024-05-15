package com.example.reminderapp.domain.usecases

import com.example.reminderapp.api.model.requests.LoginRequest
import com.example.reminderapp.common.extensions.mapToState
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.TokenModel
import com.example.reminderapp.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(
        login: String,
        password: String
    ): Flow<State<TokenModel>> = flow {
        emit(State.Loading)
        val result = repository.loginUser(
            LoginRequest(
                login = login,
                password = password
            )
        )
        emit(result.mapToState())
    }
}