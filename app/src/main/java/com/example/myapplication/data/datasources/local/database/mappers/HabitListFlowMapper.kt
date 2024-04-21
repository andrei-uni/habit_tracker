package com.example.myapplication.data.datasources.local.database.mappers

import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<List<HabitEntity>>.toModels(): Flow<List<Habit>> {
    return this.map { habitEntities ->
        habitEntities.map { it.toModel() }
    }
}