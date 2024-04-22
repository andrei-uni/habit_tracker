package com.example.myapplication.data.repositories

import com.example.myapplication.data.datasources.remote.habits_service.HabitsService
import com.example.myapplication.data.datasources.remote.habits_service.api_objects.shared.HabitApi
import com.example.myapplication.data.datasources.remote.habits_service.api_objects.shared.HabitUidApi
import com.example.myapplication.data.datasources.remote.habits_service.mappers.toApi
import com.example.myapplication.data.datasources.remote.habits_service.mappers.toModel
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.NewHabitId
import com.example.myapplication.domain.repositories.RemoteHabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteHabitsRepositoryImpl(
    private val habitsService: HabitsService,
) : RemoteHabitsRepository() {

    override suspend fun getHabits(): Flow<List<Habit>> {
        val habits: List<HabitApi> = habitsService.getHabits()

        val habitModels = habits.map { it.toModel() }

        return flowOf(habitModels)
    }

    override suspend fun addHabitWithId(habit: Habit): NewHabitId {
        val apiHabit = habit.toApi().copy(uid = null)

        val addResult: HabitUidApi = habitsService.addHabit(apiHabit)

        return addResult.uid
    }

    override suspend fun updateHabit(habit: Habit) {
        habitsService.updateHabit(habit.toApi())
    }

//    fun deleteAll() = runBlocking {
//        val habits = habitsService.getHabits()
//
//        habits.forEach {
//            habitsService.deleteHabit(HabitUidApi(it.uid!!))
//        }
//    }
}