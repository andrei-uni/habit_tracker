package com.example.myapplication.presentation.habits_list_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.CompleteHabitResult
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitNameFilter
import com.example.myapplication.domain.models.HabitSort
import com.example.myapplication.domain.usecases.CompleteHabitUseCase
import com.example.myapplication.domain.usecases.GetHabitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HabitsListViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val completeHabitUseCase: CompleteHabitUseCase,
) : ViewModel() {

    companion object {
        private val DEFAULT_HABIT_SORT = HabitSort.CREATION_DATE_NEWEST

        private val DEFAULT_HABIT_NAME_FILTER = HabitNameFilter("")
    }

    private val habitSort = MutableLiveData<HabitSort>(DEFAULT_HABIT_SORT)
    private val habitNameFilter = MutableLiveData<HabitNameFilter>(DEFAULT_HABIT_NAME_FILTER)
    private val mutableCompleteHabitResult = MutableLiveData<Pair<CompleteHabitResult, Habit>?>()

    val completeHabitResult: LiveData<Pair<CompleteHabitResult, Habit>?> = mutableCompleteHabitResult

    private val habitsFromRepo = MutableLiveData<List<Habit>>()

    private val habitsFromRepoObserver = object : Observer<List<Habit>> {

        private lateinit var getHabitsLiveData: LiveData<List<Habit>>

        fun getHabits() {
            viewModelScope.launch(Dispatchers.IO) {
                getHabitsLiveData = getHabitsUseCase().asLiveData()

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

    fun completeHabitClicked(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            val completeResult = completeHabitUseCase(habit)

            withContext(Dispatchers.Main) {
                mutableCompleteHabitResult.value = Pair(completeResult, habit)
                mutableCompleteHabitResult.value = null
            }
        }
    }

    override fun onCleared() {
        habitsFromRepoObserver.remove()
        super.onCleared()
    }
}