package com.example.myapplication.models

import java.io.Serializable
import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val timesToComplete: Int,
    val frequencyInDays: Int,
    val color: Int,
) : Serializable {
    companion object {
        val empty
            get() = Habit(
                name = "",
                description = "",
                priority = HabitPriority.LOW,
                type = HabitType.GOOD,
                timesToComplete = 0,
                frequencyInDays = 0,
                color = 0,
            )
    }
}
