package com.example.data.datasources.remote.habits_service.api_objects.shared

import com.example.data.datasources.remote.habits_service.api_objects.EnumAsIntSerializer
import kotlinx.serialization.Serializable

@Serializable(with = HabitPriorityApiSerializer::class)
enum class HabitPriorityApi(val apiValue: Int) {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
}

private class HabitPriorityApiSerializer : EnumAsIntSerializer<HabitPriorityApi>(
    HabitPriorityApi::class.java.simpleName,
    { it.apiValue },
    { v -> HabitPriorityApi.entries.first { it.apiValue == v } }
)