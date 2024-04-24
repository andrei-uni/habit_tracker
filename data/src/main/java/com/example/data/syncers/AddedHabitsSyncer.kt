package com.example.data.syncers

import com.example.data.datasources.local.database.entities.HabitEntity
import com.example.data.datasources.local.database.mappers.toModel
import com.example.data.datasources.remote.habits_service.api_objects.shared.HabitUidApi
import com.example.data.datasources.remote.habits_service.mappers.toApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddedHabitsSyncer @Inject constructor() : HabitsSyncer() {

    override val unsyncedHabitsFlow: Flow<List<HabitEntity>> by lazy {
        habitDao.observeUnsyncedAdd()
    }

    override suspend fun trySyncHabits(unsyncedHabits: List<HabitEntity>): Boolean {
        for (unsyncedHabit in unsyncedHabits) {
            val newHabitId = try {
                val habitApi = unsyncedHabit.toModel().toApi().copy(uid = null)
                val result: HabitUidApi = habitsService.addHabit(habitApi)
                result.uid
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