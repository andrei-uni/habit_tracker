package com.example.domain.usecases

import com.example.domain.models.CompleteHabitResult
import com.example.domain.models.Habit
import com.example.domain.repositories.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class CompleteHabitUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(habit: Habit): CompleteHabitResult = withContext(dispatcher) {
        val currentDate = Date()

        val updatedHabit = habitsRepository.completeHabit(habit.id, date = currentDate)

        CompleteHabitResultCalculator.getResult(updatedHabit, currentDate)
    }
}