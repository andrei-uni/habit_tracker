package com.example.myapplication.data.datasources.remote.habits_service.mappers

import com.example.myapplication.data.datasources.remote.habits_service.api_objects.shared.HabitTypeApi
import com.example.myapplication.domain.models.HabitType

fun HabitTypeApi.toModel(): HabitType {
    return when(this) {
        HabitTypeApi.GOOD -> HabitType.GOOD
        HabitTypeApi.BAD -> HabitType.BAD
    }
}

fun HabitType.toApi(): HabitTypeApi {
    return when(this) {
        HabitType.GOOD -> HabitTypeApi.GOOD
        HabitType.BAD -> HabitTypeApi.BAD
    }
}