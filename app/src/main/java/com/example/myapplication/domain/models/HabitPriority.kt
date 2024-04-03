package com.example.myapplication.domain.models

import com.example.myapplication.R

enum class HabitPriority(val resId: Int) {
    LOW(R.string.habit_priority_low),
    MEDIUM(R.string.habit_priority_medium),
    HIGH(R.string.habit_priority_high),
}
