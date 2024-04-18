package com.example.myapplication.data.repositories

import androidx.lifecycle.LiveData
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.HabitsRepository
import com.example.myapplication.domain.repositories.LocalHabitsRepository
import com.example.myapplication.domain.repositories.RemoteHabitsRepository
import com.example.myapplication.utils.Dependencies
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CompoundHabitsRepository : HabitsRepository() {

    private val remoteHabitsRepository: RemoteHabitsRepository by lazy {
        Dependencies.remoteHabitsRepository
    }

    private val localHabitsRepository: LocalHabitsRepository by lazy {
        Dependencies.localHabitsRepository
    }

    override suspend fun getHabits(): LiveData<List<Habit>> {
        return try {
            val remoteHabits = remoteHabitsRepository.getHabits()

            remoteHabits.value?.let {
                if (it.isNotEmpty())
                    saveRemoteHabits(it)
            }

            remoteHabits
        } catch (e: Exception) {
            localHabitsRepository.getHabits()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun saveRemoteHabits(habits: List<Habit>) {
        GlobalScope.launch {
            localHabitsRepository.addHabits(habits, synced = true)
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