package com.example.myapplication.data.datasources.local.database.mappers

import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.domain.models.Habit

fun HabitEntity.toModel(): Habit {
    return Habit(
        id = id,
        name = name,
        description = description,
        priority = priority.toModel(),
        type = type.toModel(),
        timesToComplete = timesToComplete,
        frequencyInDays = frequencyInDays,
        color = color,
        creationDate = creationDate,
    )
}

fun Habit.toDB(): HabitEntity {
    return HabitEntity(
        id = id,
        name = name,
        description = description,
        priority = priority.toDB(),
        type = type.toDB(),
        timesToComplete = timesToComplete,
        frequencyInDays = frequencyInDays,
        color = color,
        creationDate = creationDate,
    )
}