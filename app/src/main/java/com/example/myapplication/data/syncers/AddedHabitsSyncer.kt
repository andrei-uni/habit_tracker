package com.example.myapplication.data.syncers

import androidx.lifecycle.LiveData
import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.data.datasources.local.database.mappers.toModel

object AddedHabitsSyncer : HabitsSyncer() {

    override val unsyncedHabitsLiveData: LiveData<List<HabitEntity>> by lazy {
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