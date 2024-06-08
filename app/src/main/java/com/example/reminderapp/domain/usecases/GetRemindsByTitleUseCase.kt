package com.example.reminderapp.domain.usecases

import com.example.reminderapp.common.extensions.mapToState
import com.example.reminderapp.common.state.State
import com.example.reminderapp.domain.model.RemindsModel
import com.example.reminderapp.repository.RemindsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRemindsByTitleUseCase @Inject constructor(
    private val repository: RemindsRepository
) {
    operator fun invoke(title: String): Flow<State<RemindsModel>> = flow {
        emit(State.Loading)
        val result = repository.getRemindsByTitle(title)
        emit(result.mapToState())
    }
}