package com.example.myapplication.domain.repositories

import com.example.myapplication.domain.models.Habit

abstract class LocalHabitsRepository : HabitsRepository() {

    abstract suspend fun addHabit(habit: Habit, synced: Boolean)

    abstract suspend fun updateHabit(habit: Habit, synced: Boolean)

    abstract suspend fun addHabits(habits: List<Habit>, synced: Boolean)

    override suspend fun addHabit(habit: Habit) {
        addHabit(habit, synced = false)
    }

    override suspend fun updateHabit(habit: Habit) {
        updateHabit(habit, synced = false)
    }
}