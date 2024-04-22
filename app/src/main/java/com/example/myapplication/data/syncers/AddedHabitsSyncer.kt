package com.example.myapplication.data.syncers

import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.data.datasources.local.database.mappers.toModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddedHabitsSyncer @Inject constructor() : HabitsSyncer() {

    override val unsyncedHabitsFlow: Flow<List<HabitEntity>> by lazy {
        habitDao.observeUnsyncedAdd()
    }

    override suspend fun trySyncHabits(unsyncedHabits: List<HabitEntity>): Boolean {
        for (unsyncedHabit in unsyncedHabits) {
            val newHabitId = try {
                remoteHabitsRepository.addHabitWithId(unsyncedHabit.toModel())
            } catch (e: Exception) {
                return false
            }

            val newHabit = unsyncedHabit.copy(
                id = newHabitId,
                syncedAdd = 1,
                syncedUpdate = 1,
            )

            habitDao.deleteAndAddHabitTransaction(
                habitToDelete = unsyncedHabit,
                habitToAdd = newHabit,
            )
        }

        return true
    }
}