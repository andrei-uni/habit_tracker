package com.example.domain.usecases

import com.example.domain.models.Habit
import com.example.domain.repositories.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateHabitUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(habit: Habit) = withContext(dispatcher) {
        habitsRepository.updateHabit(habit)
    }
}