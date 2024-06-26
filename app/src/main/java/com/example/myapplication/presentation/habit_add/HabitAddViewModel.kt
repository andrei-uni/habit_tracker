package com.example.myapplication.presentation.habit_add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Habit
import com.example.domain.models.HabitPriority
import com.example.domain.models.HabitType
import com.example.domain.usecases.AddHabitUseCase
import com.example.domain.usecases.UpdateHabitUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@HiltViewModel(
    assistedFactory = HabitAddViewModelFactory::class
)
class HabitAddViewModel @AssistedInject constructor(
    private val addHabitUseCase: AddHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    @Assisted private val passedHabit: Habit?,
) : ViewModel() {

    private val mutableHabit: MutableLiveData<Habit> = MutableLiveData()
    private val mutableIsFormValid: MutableLiveData<Boolean> = MutableLiveData()
    private val mutableIsFinished: MutableLiveData<Boolean> = MutableLiveData()

    val habit: LiveData<Habit> = mutableHabit
    val isFormValid: LiveData<Boolean> = mutableIsFormValid
    val isFinished: LiveData<Boolean> = mutableIsFinished

    fun setHabit() {
        mutableHabit.value = passedHabit ?: Habit.empty
        checkValidity()
    }

    fun nameChanged(name: String) {
        mutableHabit.value = mutableHabit.value!!.copy(name = name)
        checkValidity()
    }

    fun descriptionChanged(description: String) {
        mutableHabit.value = mutableHabit.value!!.copy(description = description)
        checkValidity()
    }

    fun priorityChanged(priority: HabitPriority) {
        mutableHabit.value = mutableHabit.value!!.copy(priority = priority)
        checkValidity()
    }

    fun typeChanged(type: HabitType) {
        mutableHabit.value = mutableHabit.value!!.copy(type = type)
        checkValidity()
    }

    fun timesToCompleteChanged(timesToComplete: Int) {
        mutableHabit.value = mutableHabit.value!!.copy(timesToComplete = timesToComplete)
        checkValidity()
    }

    fun frequencyChanged(frequency: Int) {
        mutableHabit.value = mutableHabit.value!!.copy(frequencyInDays = frequency)
        checkValidity()
    }

    fun colorChanged(color: Int) {
        mutableHabit.value = mutableHabit.value!!.copy(color = color)
        checkValidity()
    }

    private fun checkValidity() {
        mutableIsFormValid.value = isValid
    }

    private val isValid: Boolean
        get() {
            val habit = habit.value ?: return false
            if (habit.name.isBlank())
                return false
            if (habit.description.isBlank())
                return false
            if (habit.timesToComplete == 0)
                return false
            if (habit.frequencyInDays == 0)
                return false
            if (habit.color == 0)
                return false
            if (habit == passedHabit)
                return false
            return true
        }

    fun savePressed() {
        if (isFormValid.value == null || !isFormValid.value!!)
            return

        val habit = habit.value?.run {
            copy(
                name = name.trim(),
                description = description.trim(),
                lastEditDate = Date()
            )
        } ?: return

        viewModelScope.launch {
            if (passedHabit == null) {
                addHabitUseCase(habit)
            } else {
                updateHabitUseCase(
                    habit.copy(
                        id = passedHabit.id,
                    )
                )
            }

            withContext(Dispatchers.Main) {
                mutableIsFinished.value = true
            }
        }
    }
}
