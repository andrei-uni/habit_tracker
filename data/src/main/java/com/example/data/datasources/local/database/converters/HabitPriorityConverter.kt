package com.example.data.datasources.local.database.converters

import androidx.room.TypeConverter
import com.example.data.datasources.local.database.entities.HabitPriorityDB

class HabitPriorityConverter {

    @TypeConverter
    fun fromHabitPriority(habitPriorityDb: HabitPriorityDB): Int {
        return habitPriorityDb.dbValue
    }

    @TypeConverter
    fun toHabitPriority(data: Int): HabitPriorityDB {
        return HabitPriorityDB.entries.first { it.dbValue == data }
    }
}