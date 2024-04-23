package com.example.myapplication.data.datasources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.data.datasources.local.database.converters.DateConverter
import com.example.myapplication.data.datasources.local.database.converters.DateListConverter
import com.example.myapplication.data.datasources.local.database.converters.HabitPriorityConverter
import com.example.myapplication.data.datasources.local.database.converters.HabitTypeConverter
import java.util.Date

@Entity(tableName = "Habits")
@TypeConverters(
    HabitTypeConverter::class,
    HabitPriorityConverter::class,
    DateConverter::class,
    DateListConverter::class,
)
data class HabitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val type: HabitTypeDB,
    val priority: HabitPriorityDB,
    val timesToComplete: Int,
    val frequencyInDays: Int,
    val color: Int,
    val lastEditDate: Date,
    val syncedAdd: Int,
    val syncedUpdate: Int,
    val doneDates: List<Date>,
)