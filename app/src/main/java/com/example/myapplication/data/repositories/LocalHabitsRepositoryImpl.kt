package com.example.myapplication.data.repositories

import com.example.myapplication.data.datasources.local.database.daos.HabitDao
import com.example.myapplication.data.datasources.local.database.mappers.toDB
import com.example.myapplication.data.datasources.local.database.mappers.toDbSynced
import com.example.myapplication.data.datasources.local.database.mappers.toDbUnsynced
import com.example.myapplication.data.datasources.local.database.mappers.toModels
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.LocalHabitsRepository
import com.example.myapplication.utils.Dependencies
import kotlinx.coroutines.flow.Flow

class LocalHabitsRepositoryImpl : LocalHabitsRepository() {

    private val habitDao: HabitDao by lazy {
        Dependencies.appDatabase.habitDao()
    }

    override suspend fun getHabits(): Flow<List<Habit>> {
        return habitDao.getAll().toModels()
    }

    override suspend fun addHabits(habits: List<Habit>, synced: Boolean) {
        val habitsToAdd = if (synced)
            habits.map { it.toDbSynced() }
        else
            habits.map { it.toDbUnsynced() }

        habitDao.addAll(habitsToAdd)
    }

    override suspend fun addHabit(habit: Habit, synced: Boolean) {
        val habitToAdd = if (synced)
            habit.toDbSynced()
        else
            habit.toDbUnsynced()

        habitDao.add(habitToAdd)
    }

    override suspend fun updateHabit(habit: Habit, synced: Boolean) {
        val oldHabit = habitDao.getById(habit.id)

        val newHabit = habit.toDB(
            syncedAdd = if (synced) 1 else oldHabit.syncedAdd,
            syncedUpdate = if (synced) 1 else 0
        )

        habitDao.update(newHabit)
    }
}