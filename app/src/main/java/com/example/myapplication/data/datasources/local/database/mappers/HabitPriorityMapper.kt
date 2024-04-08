package com.example.myapplication.data.datasources.local.database.mappers

import com.example.myapplication.data.datasources.local.database.entities.HabitPriorityDB
import com.example.myapplication.domain.models.HabitPriority

fun HabitPriorityDB.toModel(): HabitPriority {
    return when(this) {
        HabitPriorityDB.LOW -> HabitPriority.LOW
        HabitPriorityDB.MEDIUM -> HabitPriority.MEDIUM
        HabitPriorityDB.HIGH -> HabitPriority.HIGH
    }
}

fun HabitPriority.toDB(): HabitPriorityDB {
    return when(this) {
        HabitPriority.LOW -> HabitPriorityDB.LOW
        HabitPriority.MEDIUM -> HabitPriorityDB.MEDIUM
        HabitPriority.HIGH -> HabitPriorityDB.HIGH
    }
}