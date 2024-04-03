package com.example.myapplication.domain.repositories

import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitNameFilter
import com.example.myapplication.domain.models.HabitSort

typealias VoidCallback = () -> Unit

abstract class HabitsRepository {

    abstract fun getHabits(habitSort: HabitSort, habitNameFilter: HabitNameFilter?): List<Habit>

    abstract fun addHabit(habit: Habit)

    abstract fun updateHabit(habit: Habit)

    private val onChangedCallbacks = mutableListOf<VoidCallback>()

    fun addOnChangedCallback(callback: VoidCallback) {
        onChangedCallbacks.add(callback)
    }

    protected fun notifyChanged() {
        onChangedCallbacks.forEach { it.invoke() }
    }
}