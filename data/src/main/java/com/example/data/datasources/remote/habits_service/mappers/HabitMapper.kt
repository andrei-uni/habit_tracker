package com.example.data.datasources.remote.habits_service.mappers

import com.example.data.datasources.remote.habits_service.api_objects.shared.HabitApi
import com.example.domain.models.Habit
import java.util.Date

fun HabitApi.toModel(): Habit {
    return Habit(
        id = uid!!,
        name = name,
        description = description,
        priority = priority.toModel(),
        type = type.toModel(),
        timesToComplete = count,
        frequencyInDays = frequency,
        color = color,
        lastEditDate = Date(date),
        doneDates = doneDates.map { Date(it) }
    )
}

fun Habit.toApi(): HabitApi {
    return HabitApi(
        uid = id,
        name = name,
        description = description,
        priority = priority.toApi(),
        type = type.toApi(),
        count = timesToComplete,
        frequency = frequencyInDays,
        color = color,
        date = lastEditDate.time,
        doneDates = doneDates.map { it.time },
    )
}