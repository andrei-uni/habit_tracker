package com.example.data.datasources.local.database.mappers

import com.example.data.datasources.local.database.entities.HabitTypeDB
import com.example.domain.models.HabitType

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