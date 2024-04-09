package com.example.myapplication.data.datasources.local.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.datasources.local.database.entities.HabitEntity

@Dao
interface HabitDao {

    @Query("SELECT * FROM Habits")
    fun getAll(): LiveData<List<HabitEntity>>

    @Insert
    suspend fun add(habitEntity: HabitEntity)

    @Update
    suspend fun update(habitEntity: HabitEntity)
}