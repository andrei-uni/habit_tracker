package com.example.domain.repositories

import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import java.util.Date

abstract class HabitsRepository {

    abstract suspend fun getHabits(): Flow<List<Habit>>

    abstract suspend fun addHabit(habit: Habit)

    abstract suspend fun updateHabit(habit: Habit)

    abstract suspend fun completeHabit(habitId: String, date: Date): Habit
}