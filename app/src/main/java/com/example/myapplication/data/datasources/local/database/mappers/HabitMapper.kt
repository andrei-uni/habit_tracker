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
        lastEditDate = lastEditDate,
        doneDates = doneDates,
    )
}

fun Habit.toDB(syncedAdd: Int, syncedUpdate: Int): HabitEntity {
    return HabitEntity(
        id = id,
        name = name,
        description = description,
        priority = priority.toDB(),
        type = type.toDB(),
        timesToComplete = timesToComplete,
        frequencyInDays = frequencyInDays,
        color = color,
        lastEditDate = lastEditDate,
        doneDates = doneDates,
        syncedAdd = syncedAdd,
        syncedUpdate = syncedUpdate,
    )
}

fun Habit.toDbSynced(): HabitEntity {
    return toDB(syncedAdd = 1, syncedUpdate = 1)
}

fun Habit.toDbUnsynced(): HabitEntity {
    return toDB(syncedAdd = 0, syncedUpdate = 0)
}