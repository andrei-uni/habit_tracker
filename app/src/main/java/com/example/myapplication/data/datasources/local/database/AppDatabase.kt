package com.example.myapplication.data.datasources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.datasources.local.database.daos.HabitDao
import com.example.myapplication.data.datasources.local.database.entities.HabitEntity

@Database(
    entities = [HabitEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}