package com.example.myapplication.data.repositories

import com.example.myapplication.data.datasources.local.database.AppDatabase
import com.example.myapplication.data.datasources.local.database.daos.HabitDao
import com.example.myapplication.data.datasources.local.database.mappers.toDB
import com.example.myapplication.data.datasources.local.database.mappers.toDbSynced
import com.example.myapplication.data.datasources.local.database.mappers.toDbUnsynced
import com.example.myapplication.data.datasources.local.database.mappers.toModel
import com.example.myapplication.data.datasources.local.database.mappers.toModels
import com.example.myapplication.data.datasources.remote.habits_service.HabitsService
import com.example.myapplication.data.datasources.remote.habits_service.api_objects.shared.HabitDoneApi
import com.example.myapplication.data.datasources.remote.habits_service.mappers.toModel
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.HabitsRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date

class HabitsRepositoryImpl(
    private val habitsService: HabitsService,
    private val appDatabase: AppDatabase,
) : HabitsRepository() {

    private val habitDao: HabitDao by lazy {
        appDatabase.habitDao()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun getHabits(): Flow<List<Habit>> {
        GlobalScope.launch {
            saveRemoteHabits()
        }

        return habitDao.getAll().toModels()
    }

    private suspend fun saveRemoteHabits() {
        try {
            val remoteHabits = habitsService.getHabits().map { it.toModel() }

            if (remoteHabits.isEmpty())
                return

            habitDao.addAll(
                remoteHabits.map { it.toDbSynced() }
            )

        } catch (e: Exception) {
            return
        }
    }

    override suspend fun addHabit(habit: Habit) {
        habitDao.add(habit.toDbUnsynced())
    }

    override suspend fun updateHabit(habit: Habit) {
        val oldHabit = habitDao.getById(habit.id)

        val newHabit = habit.toDB(
            syncedAdd = oldHabit.syncedAdd,
            syncedUpdate = 0,
        )

        habitDao.update(newHabit)
    }

    override suspend fun completeHabit(habitId: String, date: Date): Habit {
        val habitEntity = habitDao.getById(habitId)

        val newHabitEntity = habitEntity.copy(
            doneDates = habitEntity.doneDates + date,
        )

        habitDao.update(newHabitEntity)

        try {
            val habitDoneApi = HabitDoneApi(
                date = date.time,
                habitUid = habitId,
            )
            habitsService.doHabit(habitDoneApi)
        } catch (_: Exception) { }

        return newHabitEntity.toModel()
    }

//    fun deleteAll() = runBlocking {
//        val habits = habitsService.getHabits()
//
//        habits.forEach {
//            habitsService.deleteHabit(HabitUidApi(it.uid!!))
//        }
//    }
}