package com.example.myapplication.domain.repositories

import com.example.myapplication.domain.models.Habit
import kotlinx.coroutines.flow.Flow

abstract class HabitsRepository {

    abstract suspend fun getHabits(): Flow<List<Habit>>

    abstract suspend fun addHabit(habit: Habit)

    abstract suspend fun updateHabit(habit: Habit)
}