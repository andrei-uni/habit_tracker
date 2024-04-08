package com.example.myapplication.data.datasources.local.database.converters

import androidx.room.TypeConverter
import com.example.myapplication.data.datasources.local.database.entities.HabitTypeDB

class HabitTypeConverter {

    @TypeConverter
    fun fromHabitType(habitTypeDb: HabitTypeDB): Int {
        return habitTypeDb.dbValue
    }

    @TypeConverter
    fun toHabitType(data: Int): HabitTypeDB {
        return HabitTypeDB.entries.first { it.dbValue == data }
    }
}