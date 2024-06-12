package com.example.reminderapp.domain.usecases

import com.example.reminderapp.common.extensions.mapToState
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.RemindModel
import com.example.reminderapp.repository.RemindsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRemindUseCase @Inject constructor(
    private val repository: RemindsRepository
) {
    operator fun invoke(id: Int): Flow<State<RemindModel>> = flow {
        emit(State.Loading)
        val result = repository.getRemindById(id)
        emit(result.mapToState())
    }
}