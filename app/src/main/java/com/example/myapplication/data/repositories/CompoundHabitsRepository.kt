package com.example.myapplication.data.repositories

import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.HabitsRepository
import com.example.myapplication.domain.repositories.LocalHabitsRepository
import com.example.myapplication.domain.repositories.RemoteHabitsRepository
import com.example.myapplication.utils.Dependencies
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CompoundHabitsRepository : HabitsRepository() {

    private val remoteHabitsRepository: RemoteHabitsRepository by lazy {
        Dependencies.remoteHabitsRepository
    }

    private val localHabitsRepository: LocalHabitsRepository by lazy {
        Dependencies.localHabitsRepository
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun getHabits(): Flow<List<Habit>> {
        GlobalScope.launch {
            saveRemoteHabits()
        }

        return localHabitsRepository.getHabits()
    }

    private suspend fun saveRemoteHabits() {
        try {
            val remoteHabits = remoteHabitsRepository.getHabits().firstOrNull()

            if (remoteHabits.isNullOrEmpty())
                return

            localHabitsRepository.addHabits(remoteHabits, synced = true)

        } catch (e: Exception) {
            return
        }
    }

    override suspend fun addHabit(habit: Habit) {
        try {
            val newHabitId = remoteHabitsRepository.addHabitWithId(habit)

            localHabitsRepository.addHabit(habit.copy(id = newHabitId), synced = true)

        } catch (e: Exception) {
            localHabitsRepository.addHabit(habit, synced = false)
        }
    }

    override suspend fun updateHabit(habit: Habit) {
        try {
            remoteHabitsRepository.updateHabit(habit)

            localHabitsRepository.updateHabit(habit, synced = true)

        } catch (e: Exception) {
            localHabitsRepository.updateHabit(habit, synced = false)
        }
    }
}