package com.example.reminderapp.domain.usecases

import com.example.reminderapp.api.model.requests.DeleteRemindsRequest
import com.example.reminderapp.common.extensions.mapToState
import com.example.reminderapp.common.state.State
import com.example.reminderapp.repository.RemindsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteRemindUseCase @Inject constructor(
    private val repository: RemindsRepository
) {
    operator fun invoke(
        ids: List<Int>
    ): Flow<State<Unit>> = flow {
        emit(State.Loading)
        val request = ids.map { it }
        val result = repository.deleteReminds(
            DeleteRemindsRequest(request)
        )
        emit(result.mapToState())
    }
}