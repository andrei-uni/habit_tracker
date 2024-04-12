package com.example.myapplication.presentation.habits_list_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitNameFilter
import com.example.myapplication.domain.models.HabitSort
import com.example.myapplication.presentation.home.habitsRepository


class HabitsListViewModel : ViewModel() {

    companion object {
        private val DEFAULT_HABIT_SORT = HabitSort.CREATION_DATE_NEWEST

        private val DEFAULT_HABIT_NAME_FILTER = HabitNameFilter("")
    }

    private val habitSort = MutableLiveData<HabitSort>(DEFAULT_HABIT_SORT)
    private val habitNameFilter = MutableLiveData<HabitNameFilter>(DEFAULT_HABIT_NAME_FILTER)

    private val habitsFromRepo = MutableLiveData<List<Habit>>()

    private val habitsFromRepoObserver = object : Observer<List<Habit>> {

        private val getHabits: LiveData<List<Habit>> by lazy {
            habitsRepository.getHabits()
        }

        fun observe() {
            getHabits.observeForever(this)
        }

        fun remove() {
            getHabits.removeObserver(this)
        }

        override fun onChanged(value: List<Habit>) {
            habitsFromRepo.postValue(value)
        }
    }

    private val habitsMediator = MediatorLiveData<List<Habit>>().apply {
        addSource(habitsFromRepo) { updateHabits(this) }
        addSource(habitSort) { updateHabits(this) }
        addSource(habitNameFilter) { updateHabits(this) }

        updateHabits(this)
    }

    val habits: LiveData<List<Habit>> = habitsMediator

    private fun updateHabits(liveData: MutableLiveData<List<Habit>>) {
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
            HabitSort.CREATION_DATE_NEWEST -> filtered.sortedByDescending { it.creationDate }
            HabitSort.CREATION_DATE_OLDEST -> filtered.sortedBy { it.creationDate }
        }

        liveData.postValue(sorted)
    }

    fun loadHabits() {
        habitsFromRepoObserver.observe()
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