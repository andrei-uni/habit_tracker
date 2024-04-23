package com.example.myapplication.domain.models

import java.io.Serializable
import java.util.Date
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
    val lastEditDate: Date,
    val doneDates: List<Date>
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
                lastEditDate = Date(0),
                doneDates = emptyList(),
            )
    }
}
