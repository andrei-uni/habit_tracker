package com.example.myapplication.data

import com.example.myapplication.models.Habit

typealias VoidCallback = () -> Unit

class HabitsRepository {

    private val onChangedCallbacks = mutableListOf<VoidCallback>()

    val habits = mutableListOf<Habit>()

    fun addHabit(habit: Habit) {
        habits.add(0, habit)
        notifyChanged()
    }

    fun updateHabit(habit: Habit) {
        val index = habits.indexOfFirst { it.id == habit.id }
        habits[index] = habit
        notifyChanged()
    }

    fun addOnChangedCallback(callback: VoidCallback) {
        onChangedCallbacks.add(callback)
    }

    private fun notifyChanged() {
        onChangedCallbacks.forEach { it.invoke() }
    }
}