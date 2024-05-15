package com.example.reminderapp.domain.usecases

import com.example.reminderapp.api.model.requests.RegistrationRequest
import com.example.reminderapp.common.extensions.mapToState
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.TokenModel
import com.example.reminderapp.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(
        login: String,
        password: String,
        email: String
    ): Flow<State<TokenModel>> = flow {
        emit(State.Loading)
        val result = repository.registrationUser(
            RegistrationRequest(
                login = login,
                password = password,
                email = email
            )
        )
        emit(result.mapToState())
    }
}