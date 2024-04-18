package com.example.myapplication.data.syncers

import androidx.lifecycle.LiveData
import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.data.datasources.local.database.mappers.toModel

object UpdatedHabitsSyncer : HabitsSyncer() {

    override val unsyncedHabitsLiveData: LiveData<List<HabitEntity>> by lazy {
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