package com.example.myapplication.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.datasources.remote.habits_service.HabitsService
import com.example.myapplication.data.datasources.remote.habits_service.api_objects.shared.HabitApi
import com.example.myapplication.data.datasources.remote.habits_service.api_objects.shared.HabitUidApi
import com.example.myapplication.data.datasources.remote.habits_service.mappers.toApi
import com.example.myapplication.data.datasources.remote.habits_service.mappers.toModel
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.NewHabitId
import com.example.myapplication.domain.repositories.RemoteHabitsRepository
import com.example.myapplication.utils.Dependencies

class RemoteHabitsRepositoryImpl : RemoteHabitsRepository() {

    private val habitsService: HabitsService by lazy {
        Dependencies.habitsService
    }

    private val mutableHabits = MutableLiveData<List<Habit>>()

    override suspend fun getHabits(): LiveData<List<Habit>> {
        val habits: List<HabitApi> = habitsService.getHabits()

        mutableHabits.postValue(habits.map { it.toModel() })

        return mutableHabits
    }

    override suspend fun addHabitWithId(habit: Habit): NewHabitId {
        val apiHabit = habit.toApi().copy(uid = null)

        val addResult: HabitUidApi = habitsService.addHabit(apiHabit)

        mutableHabits.apply {
            val habits = value?.toMutableList() ?: mutableListOf()
            habits.add(habit.copy(id = addResult.uid))
            postValue(habits)
        }

        return addResult.uid
    }

    override suspend fun updateHabit(habit: Habit) {
        habitsService.updateHabit(habit.toApi())

        mutableHabits.apply {
            val habits = value?.toMutableList() ?: mutableListOf()
            val index = habits.indexOfFirst { it.id == habit.id }
            habits[index] = habit
            postValue(habits)
        }
    }

//    fun deleteAll() = runBlocking {
//        val habits = habitsService.getHabits()
//
//        habits.forEach {
//            habitsService.deleteHabit(HabitUidApi(it.uid!!))
//        }
//    }
}