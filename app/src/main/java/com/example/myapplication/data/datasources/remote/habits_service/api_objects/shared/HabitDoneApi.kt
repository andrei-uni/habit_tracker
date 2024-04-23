package com.example.myapplication.data.datasources.remote.habits_service.api_objects.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitDoneApi(
    val date: Long,
    @SerialName("habit_uid")
    val habitUid: String,
)