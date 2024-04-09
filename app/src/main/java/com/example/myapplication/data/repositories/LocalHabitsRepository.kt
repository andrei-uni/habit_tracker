package com.example.myapplication.data.repositories

import androidx.lifecycle.LiveData
import com.example.myapplication.data.datasources.local.database.AppDatabase
import com.example.myapplication.data.datasources.local.database.daos.HabitDao
import com.example.myapplication.data.datasources.local.database.mappers.toDB
import com.example.myapplication.data.datasources.local.database.mappers.toModels
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.HabitsRepository
import com.example.myapplication.utils.Dependencies

class LocalHabitsRepository : HabitsRepository() {

    private val database: AppDatabase by lazy {
        Dependencies.appDatabase
    }

    private val habitDao: HabitDao get() = database.habitDao()

    override fun getHabits(): LiveData<List<Habit>> {
        return habitDao.getAll().toModels()
    }

    override suspend fun addHabit(habit: Habit) {
        habitDao.add(habit.toDB())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(habit.toDB())
    }
}