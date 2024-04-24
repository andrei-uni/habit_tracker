package com.example.data.datasources.remote.habits_service.api_objects.shared

import com.example.data.datasources.remote.habits_service.api_objects.EnumAsIntSerializer
import kotlinx.serialization.Serializable

@Serializable(with = HabitTypeApiSerializer::class)
enum class HabitTypeApi(val apiValue: Int) {
    GOOD(0),
    BAD(1),
}

private class HabitTypeApiSerializer : EnumAsIntSerializer<HabitTypeApi>(
    HabitTypeApi::class.java.simpleName,
    { it.apiValue },
    { v -> HabitTypeApi.entries.first { it.apiValue == v } }
)