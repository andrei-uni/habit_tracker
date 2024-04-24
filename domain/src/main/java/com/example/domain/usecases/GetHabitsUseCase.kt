package com.example.domain.usecases

import com.example.domain.models.Habit
import com.example.domain.repositories.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Flow<List<Habit>> = withContext(dispatcher) {
        habitsRepository.getHabits()
    }
}