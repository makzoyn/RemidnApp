package com.example.reminderapp.domain.usecases

import com.example.reminderapp.api.model.requests.CreateRemindRequest
import com.example.reminderapp.common.extensions.mapToState
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.RemindModel
import com.example.reminderapp.repository.RemindsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateRemindUseCase @Inject constructor(
    private val repository: RemindsRepository
) {
    operator fun invoke(
        title: String,
        description: String?,
        time: String?,
        date: String?
    ): Flow<State<RemindModel>> = flow {
        emit(State.Loading)
        val result = repository.createRemind(
            CreateRemindRequest(
                title = title,
                description = description,
                time = time,
                date = date
            )
        )
        emit(result.mapToState())
    }
}