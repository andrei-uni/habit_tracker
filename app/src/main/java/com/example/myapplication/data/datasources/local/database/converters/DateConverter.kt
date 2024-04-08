package com.example.myapplication.data.datasources.local.database.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(data: Long): Date {
        return Date(data)
    }
}