package com.example.myapplication.data.syncers

import androidx.lifecycle.LiveData
import com.example.myapplication.data.datasources.local.database.daos.HabitDao
import com.example.myapplication.data.datasources.local.database.entities.HabitEntity
import com.example.myapplication.domain.repositories.RemoteHabitsRepository
import com.example.myapplication.utils.Dependencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

abstract class HabitsSyncer : CoroutineScope {

    abstract val unsyncedHabitsLiveData: LiveData<List<HabitEntity>>

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

    private var isSyncScheduled = false

    fun init() {
        unsyncedHabitsLiveData.observeForever { unsyncedHabits ->
            if (unsyncedHabits.isNotEmpty()) {
                launch {
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

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun syncHabits() {
        GlobalScope.launch {
            val unsyncedHabits = unsyncedHabitsLiveData.value ?: run {
                isSyncScheduled = false
                return@launch
            }

            val syncSuccessful = trySyncHabits(unsyncedHabits)

            if (syncSuccessful) {
                isSyncScheduled = false
            } else {
                delay(syncRetryInterval)
                syncHabits()
            }
        }
    }
}