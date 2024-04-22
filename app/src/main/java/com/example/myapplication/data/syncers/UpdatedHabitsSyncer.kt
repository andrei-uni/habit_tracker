package com.example.myapplication.data.syncers

import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.data.datasources.local.database.mappers.toModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatedHabitsSyncer @Inject constructor() : HabitsSyncer() {

    override val unsyncedHabitsFlow: Flow<List<HabitEntity>> by lazy {
        habitDao.observeUnsyncedUpdate()
    }

    override suspend fun trySyncHabits(unsyncedHabits: List<HabitEntity>): Boolean {
        for (unsyncedHabit in unsyncedHabits) {
            try {
                remoteHabitsRepository.updateHabit(unsyncedHabit.toModel())
            } catch (e: Exception) {
                return false
            }

            habitDao.update(unsyncedHabit.copy(syncedUpdate = 1))
        }

        return true
    }
}