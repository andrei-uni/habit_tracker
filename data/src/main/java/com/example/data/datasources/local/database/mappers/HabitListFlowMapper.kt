package com.example.data.datasources.local.database.mappers

import com.example.data.datasources.local.database.entities.HabitEntity
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<List<HabitEntity>>.toModels(): Flow<List<Habit>> {
    return this.map { habitEntities ->
        habitEntities.map { it.toModel() }
    }
}