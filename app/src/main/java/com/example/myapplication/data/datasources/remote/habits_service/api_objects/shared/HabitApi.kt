package com.example.myapplication.data.datasources.remote.habits_service.api_objects.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitApi(
    val uid: String?,
    @SerialName("title")
    val name: String,
    val description: String,
    val type: HabitTypeApi,
    val priority: HabitPriorityApi,
    val frequency: Int,
    val date: Long,
    val count: Int,
    val color: Int,
    @SerialName("done_dates")
    val doneDates: List<Int>,
)