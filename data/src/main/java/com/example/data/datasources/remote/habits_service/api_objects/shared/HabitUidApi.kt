package com.example.data.datasources.remote.habits_service.api_objects.shared

import kotlinx.serialization.Serializable

@Serializable
data class HabitUidApi(
    val uid: String,
)