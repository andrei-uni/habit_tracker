package com.example.myapplication.domain.repositories

import com.example.myapplication.domain.models.Habit

typealias NewHabitId = String

abstract class RemoteHabitsRepository : HabitsRepository() {

    abstract suspend fun addHabitWithId(habit: Habit): NewHabitId

    override suspend fun addHabit(habit: Habit) {
        addHabitWithId(habit)
    }
}