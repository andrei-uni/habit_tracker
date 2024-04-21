package com.example.myapplication.data.syncers

import com.example.myapplication.data.datasources.local.database.daos.HabitDao
import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.domain.repositories.RemoteHabitsRepository
import com.example.myapplication.utils.Dependencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

abstract class HabitsSyncer : CoroutineScope {

    abstract val unsyncedHabitsFlow: Flow<List<HabitEntity>>

    abstract suspend fun trySyncHabits(unsyncedHabits: List<HabitEntity>): Boolean

    open val syncRetryInterval = 10.toDuration(DurationUnit.SECONDS)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    protected val remoteHabitsRepository: RemoteHabitsRepository by lazy {
        Dependencies.remoteHabitsRepository
    }

    protected val habitDao: HabitDao by lazy {
        Dependencies.appDatabase.habitDao()
    }

    private lateinit var unsyncedHabitsStateFlow: StateFlow<List<HabitEntity>>

    private var isSyncScheduled = false

    fun init() {
        launch {
            unsyncedHabitsStateFlow = unsyncedHabitsFlow.stateIn(this)

            unsyncedHabitsStateFlow.collect { unsyncedHabits ->
                if (unsyncedHabits.isNotEmpty()) {
                    unsyncedHabitsChanged()
                }
            }
        }
    }

    private suspend fun unsyncedHabitsChanged() {
        if (isSyncScheduled)
            return

        isSyncScheduled = true

        syncHabits()
    }

    private suspend fun syncHabits() {
        val unsyncedHabits = unsyncedHabitsStateFlow.value

        val syncSuccessful = trySyncHabits(unsyncedHabits)

        if (syncSuccessful) {
            isSyncScheduled = false
            return
        }

        delay(syncRetryInterval)
        syncHabits()
    }
}