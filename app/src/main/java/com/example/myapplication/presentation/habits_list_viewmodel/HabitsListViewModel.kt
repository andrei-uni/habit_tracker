package com.example.myapplication.presentation.habits_list_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitNameFilter
import com.example.myapplication.domain.models.HabitSort
import com.example.myapplication.domain.repositories.HabitsRepository
import com.example.myapplication.utils.Dependencies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HabitsListViewModel : ViewModel() {

    companion object {
        private val DEFAULT_HABIT_SORT = HabitSort.CREATION_DATE_NEWEST

        private val DEFAULT_HABIT_NAME_FILTER = HabitNameFilter("")
    }

    private val habitsRepository: HabitsRepository by lazy {
        Dependencies.habitsRepository
    }

    private val habitSort = MutableLiveData<HabitSort>(DEFAULT_HABIT_SORT)
    private val habitNameFilter = MutableLiveData<HabitNameFilter>(DEFAULT_HABIT_NAME_FILTER)

    private val habitsFromRepo = MutableLiveData<List<Habit>>()

    private val habitsFromRepoObserver = object : Observer<List<Habit>> {

        private lateinit var getHabitsLiveData: LiveData<List<Habit>>

        fun getHabits() {
            viewModelScope.launch(Dispatchers.IO) {
                getHabitsLiveData = habitsRepository.getHabits()

                withContext(Dispatchers.Main) {
                    observe()
                }
            }
        }

        fun observe() {
            getHabitsLiveData.observeForever(this)
        }

        fun remove() {
            getHabitsLiveData.removeObserver(this)
        }

        override fun onChanged(value: List<Habit>) {
            habitsFromRepo.postValue(value)
        }
    }

    private val habitsMediator = MediatorLiveData<List<Habit>>().apply {
        addSource(habitsFromRepo) { updateHabits() }
        addSource(habitSort) { updateHabits() }
        addSource(habitNameFilter) { updateHabits() }

        updateHabits()
    }

    val habits: LiveData<List<Habit>> = habitsMediator

    private fun updateHabits() {
        val habits = habitsFromRepo.value ?: return
        val habitSort = habitSort.value ?: return
        val habitNameFilter = habitNameFilter.value ?: return

        val filtered = if (habitNameFilter.startsWith.isBlank())
            habits
        else
            habits.filter {
                it.name.startsWith(habitNameFilter.startsWith, true)
            }

        val sorted = when (habitSort) {
            HabitSort.CREATION_DATE_NEWEST -> filtered.sortedByDescending { it.lastEditDate }
            HabitSort.CREATION_DATE_OLDEST -> filtered.sortedBy { it.lastEditDate }
        }

        habitsMediator.value = sorted
    }

    fun loadHabits() {
        habitsFromRepoObserver.getHabits()
    }

    fun setSort(habitSort: HabitSort) {
        this.habitSort.value = habitSort
    }

    fun removeSort() {
        habitSort.value = DEFAULT_HABIT_SORT
    }

    fun setHabitNameFilter(habitNameFilter: HabitNameFilter) {
        this.habitNameFilter.value = habitNameFilter
    }

    fun removeHabitNameFilter() {
        habitNameFilter.value = DEFAULT_HABIT_NAME_FILTER
    }

    override fun onCleared() {
        habitsFromRepoObserver.remove()
        super.onCleared()
    }
}