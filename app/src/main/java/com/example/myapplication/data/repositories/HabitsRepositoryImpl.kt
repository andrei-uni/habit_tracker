package com.example.myapplication.data.repositories

import com.example.myapplication.data.datasources.SimpleHabitsDataSource
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitNameFilter
import com.example.myapplication.domain.models.HabitSort
import com.example.myapplication.domain.repositories.HabitsRepository

class HabitsRepositoryImpl : HabitsRepository() {

    private val habitsDataSource = SimpleHabitsDataSource()

    override fun getHabits(habitSort: HabitSort, habitNameFilter: HabitNameFilter?): List<Habit> {
        return habitsDataSource.getHabits(habitSort, habitNameFilter)
    }

    override fun addHabit(habit: Habit) {
        habitsDataSource.addHabit(habit)
        notifyChanged()
    }

    override fun updateHabit(habit: Habit) {
        habitsDataSource.updateHabit(habit)
        notifyChanged()
    }
}