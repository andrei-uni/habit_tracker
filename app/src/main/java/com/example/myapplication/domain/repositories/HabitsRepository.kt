package com.example.myapplication.domain.repositories

import androidx.lifecycle.LiveData
import com.example.myapplication.domain.models.Habit

abstract class HabitsRepository {

    abstract fun getHabits(): LiveData<List<Habit>>

    abstract fun addHabit(habit: Habit)

    abstract fun updateHabit(habit: Habit)
}