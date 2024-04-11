package com.example.myapplication.data.datasources.local.database.mappers

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.domain.models.Habit

fun LiveData<List<HabitEntity>>.toModels(): LiveData<List<Habit>> {
    return this.map { habitEntities ->
        habitEntities.map { it.toModel() }
    }
}