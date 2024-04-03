package com.example.myapplication.data.datasources

import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitNameFilter
import com.example.myapplication.domain.models.HabitSort

class SimpleHabitsDataSource {

    private val habits = mutableListOf<Habit>()

    fun getHabits(habitSort: HabitSort, habitNameFilter: HabitNameFilter?): List<Habit> {
        val filtered = if (habitNameFilter == null) {
            habits
        } else {
            habits.filter {
                it.name.startsWith(habitNameFilter.startsWith, true)
            }
        }

        val sorted = when (habitSort) {
            HabitSort.CREATION_DATE_NEWEST -> filtered.sortedByDescending { it.creationDate }
            HabitSort.CREATION_DATE_OLDEST -> filtered.sortedBy { it.creationDate }
        }

        return sorted
    }

    fun addHabit(habit: Habit) {
        habits.add(habit)
    }

    fun updateHabit(habit: Habit) {
        val index = habits.indexOfFirst { it.id == habit.id }
        habits[index] = habit
    }
}