package com.example.myapplication.presentation.habits_list_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    init {
        habitsRepository.addOnChangedCallback {
            loadHabits()
        }
    }

    private val mutableHabits: MutableLiveData<List<Habit>> = MutableLiveData()

    val habits: LiveData<List<Habit>> = mutableHabits

    private var habitSort = DEFAULT_HABIT_SORT

    private var habitNameFilter = DEFAULT_HABIT_NAME_FILTER

    fun loadHabits() {
        val nameFilter: HabitNameFilter? =
            if (habitNameFilter.startsWith.isBlank()) null else habitNameFilter

        val habits = habitsRepository.getHabits(habitSort, nameFilter)

        mutableHabits.postValue(habits)
    }

    fun setSort(habitSort: HabitSort) {
        this.habitSort = habitSort
    }

    fun removeSort() {
        habitSort = DEFAULT_HABIT_SORT
    }

    fun setHabitNameFilter(habitNameFilter: HabitNameFilter) {
        this.habitNameFilter = habitNameFilter
    }

    fun removeHabitNameFilter() {
        habitNameFilter = DEFAULT_HABIT_NAME_FILTER
    }
}