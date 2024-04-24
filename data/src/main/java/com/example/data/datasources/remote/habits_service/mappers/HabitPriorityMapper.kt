package com.example.data.datasources.remote.habits_service.mappers

import com.example.data.datasources.remote.habits_service.api_objects.shared.HabitPriorityApi
import com.example.domain.models.HabitPriority

fun HabitPriorityApi.toModel(): HabitPriority {
    return when(this) {
        HabitPriorityApi.LOW -> HabitPriority.LOW
        HabitPriorityApi.MEDIUM -> HabitPriority.MEDIUM
        HabitPriorityApi.HIGH -> HabitPriority.HIGH
    }
}

fun HabitPriority.toApi(): HabitPriorityApi {
    return when(this) {
        HabitPriority.LOW -> HabitPriorityApi.LOW
        HabitPriority.MEDIUM -> HabitPriorityApi.MEDIUM
        HabitPriority.HIGH -> HabitPriorityApi.HIGH
    }
}