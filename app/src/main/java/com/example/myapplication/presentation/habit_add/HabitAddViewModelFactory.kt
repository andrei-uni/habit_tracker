package com.example.myapplication.presentation.habit_add

import com.example.domain.models.Habit
import dagger.assisted.AssistedFactory

@AssistedFactory
interface HabitAddViewModelFactory {
    fun create(passedHabit: Habit?): HabitAddViewModel
}