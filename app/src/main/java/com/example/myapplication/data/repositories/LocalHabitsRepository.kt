package com.example.myapplication.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.myapplication.data.datasources.local.database.AppDatabase
import com.example.myapplication.data.datasources.local.database.daos.HabitDao
import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.data.datasources.local.database.mappers.toDB
import com.example.myapplication.data.datasources.local.database.mappers.toModel
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.HabitsRepository
import com.example.myapplication.utils.Dependencies

class LocalHabitsRepository : HabitsRepository() {

    private val database: AppDatabase by lazy {
        Dependencies.appDatabase
    }

    private val habitDao: HabitDao get() = database.habitDao()

    override fun getHabits(): LiveData<List<Habit>> {
        return habitDao.getAll().map(::toModels)
    }

    override fun addHabit(habit: Habit) {
        habitDao.add(habit.toDB())
    }

    override fun updateHabit(habit: Habit) {
        habitDao.update(habit.toDB())
    }

    private fun toModels(habitEntities: List<HabitEntity>): List<Habit> {
        return habitEntities.map { it.toModel() }
    }
}