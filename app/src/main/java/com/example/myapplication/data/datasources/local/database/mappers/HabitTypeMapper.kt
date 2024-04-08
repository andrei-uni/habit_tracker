package com.example.myapplication.data.datasources.local.database.mappers

import com.example.myapplication.data.datasources.local.database.entities.HabitTypeDB
import com.example.myapplication.domain.models.HabitType

fun HabitTypeDB.toModel(): HabitType {
    return when(this) {
        HabitTypeDB.GOOD -> HabitType.GOOD
        HabitTypeDB.BAD -> HabitType.BAD
    }
}

fun HabitType.toDB(): HabitTypeDB {
    return when(this) {
        HabitType.GOOD -> HabitTypeDB.GOOD
        HabitType.BAD -> HabitTypeDB.BAD
    }
}