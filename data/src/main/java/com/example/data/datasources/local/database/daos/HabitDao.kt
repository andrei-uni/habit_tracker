package com.example.data.datasources.local.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.datasources.local.database.entities.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM Habits")
    fun getAll(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM Habits WHERE id = :id")
    fun getById(id: String): HabitEntity

    @Insert
    suspend fun add(habitEntity: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(habitEntities: List<HabitEntity>)

    @Update
    suspend fun update(habitEntity: HabitEntity)

    @Delete
    suspend fun delete(habitEntity: HabitEntity)

    @Query("SELECT * FROM Habits WHERE syncedAdd = 0")
    fun observeUnsyncedAdd(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM Habits WHERE syncedAdd = 1 AND syncedUpdate = 0")
    fun observeUnsyncedUpdate(): Flow<List<HabitEntity>>

    @Transaction
    suspend fun deleteAndAddHabitTransaction(
        habitToDelete: HabitEntity,
        habitToAdd: HabitEntity,
    ) {
        delete(habitToDelete)
        add(habitToAdd)
    }
}