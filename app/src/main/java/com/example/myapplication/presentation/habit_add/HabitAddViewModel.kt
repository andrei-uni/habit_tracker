package com.example.myapplication.presentation.habit_add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitPriority
import com.example.myapplication.domain.models.HabitType
import com.example.myapplication.presentation.home.habitsRepository
import java.util.Date

class HabitAddViewModel(private val passedHabit: Habit?) : ViewModel() {

    private val habit = passedHabit ?: Habit.empty

    private val mutableName: MutableLiveData<String> = MutableLiveData()
    private val mutableDescription: MutableLiveData<String> = MutableLiveData()
    private val mutablePriority: MutableLiveData<HabitPriority> = MutableLiveData()
    private val mutableType: MutableLiveData<HabitType> = MutableLiveData()
    private val mutableTimesToComplete: MutableLiveData<String> = MutableLiveData()
    private val mutableFrequency: MutableLiveData<String> = MutableLiveData()
    private val mutableColor: MutableLiveData<Int> = MutableLiveData()
    private val mutableIsFormValid: MutableLiveData<Boolean> = MutableLiveData()
    private val mutableIsFinished: MutableLiveData<Boolean> = MutableLiveData()

    val name: LiveData<String> = mutableName.distinctUntilChanged()
    val description: LiveData<String> = mutableDescription.distinctUntilChanged()
    val priority: LiveData<HabitPriority> = mutablePriority.distinctUntilChanged()
    val type: LiveData<HabitType> = mutableType.distinctUntilChanged()
    val timesToComplete: LiveData<String> = mutableTimesToComplete.distinctUntilChanged()
    val frequency: LiveData<String> = mutableFrequency.distinctUntilChanged()
    val color: LiveData<Int> = mutableColor
    val isFormValid: LiveData<Boolean> = mutableIsFormValid
    val isFinished: LiveData<Boolean> = mutableIsFinished

    fun setHabit() {
        mutableName.value = habit.name
        mutableDescription.value = habit.description
        mutablePriority.value = habit.priority
        mutableType.value = habit.type
        mutableTimesToComplete.value = habit.timesToComplete.toString()
        mutableFrequency.value = habit.frequencyInDays.toString()
        mutableColor.value = habit.color
    }

    fun nameChanged(name: String) {
        mutableName.value = name
        checkValidity()
    }

    fun descriptionChanged(description: String) {
        mutableDescription.value = description
        checkValidity()
    }

    fun priorityChanged(priority: HabitPriority) {
        mutablePriority.value = priority
        checkValidity()
    }

    fun typeChanged(type: HabitType) {
        mutableType.value = type
        checkValidity()
    }

    fun timesToCompleteChanged(timesToComplete: String) {
        mutableTimesToComplete.value = timesToComplete
        checkValidity()
    }

    fun frequencyChanged(frequency: String) {
        mutableFrequency.value = frequency
        checkValidity()
    }

    fun colorChanged(color: Int) {
        mutableColor.value = color
        checkValidity()
    }

    private fun checkValidity() {
        mutableIsFormValid.value = isValid
    }

    private val isValid: Boolean
        get() {
            if (name.value.isNullOrBlank())
                return false
            if (description.value.isNullOrBlank())
                return false
            if (timesToComplete.value.isNullOrBlank() || timesToComplete.value!!.toInt() == 0)
                return false
            if (frequency.value.isNullOrBlank() || frequency.value!!.toInt() == 0)
                return false
            if (color.value == 0)
                return false
            return true
        }

    fun savePressed() {
        val newHabit = Habit(
            name = name.value!!.trim(),
            description = description.value!!.trim(),
            priority = priority.value!!,
            type = type.value!!,
            timesToComplete = timesToComplete.value!!.toInt(),
            frequencyInDays = frequency.value!!.toInt(),
            color = color.value!!,
            creationDate = Date(),
        )

        if (passedHabit == null) {
            habitsRepository.addHabit(newHabit)
        } else {
            habitsRepository.updateHabit(
                newHabit.copy(
                    id = passedHabit.id,
                    creationDate = passedHabit.creationDate,
                )
            )
        }

        mutableIsFinished.value = true
    }
}
