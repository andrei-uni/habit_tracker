package com.example.data.syncers

import com.example.data.datasources.local.database.entities.HabitEntity
import com.example.data.datasources.local.database.mappers.toModel
import com.example.data.datasources.remote.habits_service.mappers.toApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatedHabitsSyncer @Inject constructor() : HabitsSyncer() {

    override val unsyncedHabitsFlow: Flow<List<HabitEntity>> by lazy {
        habitDao.observeUnsyncedUpdate()
    }

    override suspend fun trySyncHabits(unsyncedHabits: List<HabitEntity>): Boolean {
        for (unsyncedHabit in unsyncedHabits) {
            try {
                val habitApi = unsyncedHabit.toModel().toApi()
                habitsService.updateHabit(habitApi)
            } catch (e: Exception) {
                return false
            }

            habitDao.update(unsyncedHabit.copy(syncedUpdate = 1))
        }

        return true
    }
}