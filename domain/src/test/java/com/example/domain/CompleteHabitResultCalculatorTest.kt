package com.example.domain

import com.example.domain.models.CompletionReached
import com.example.domain.models.CompletionUnreached
import com.example.domain.models.Habit
import com.example.domain.usecases.CompleteHabitResultCalculator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Date

class CompleteHabitResultCalculatorTest {

    private val completeHabitResultCalculator = CompleteHabitResultCalculator

    @Test
    fun `simple case`() {
        val currentDate = createDate(2000, 1, 3)

        val habit = Habit.empty.copy(
            lastEditDate = createDate(2000, 1, 1),
            frequencyInDays = 3,
            timesToComplete = 5,
            doneDates = listOf(createDate(2000, 1, 2), currentDate)
        )

        val result = completeHabitResultCalculator.getResult(habit, currentDate)

        assertEquals(CompletionUnreached(timesLeft = 3), result)
    }

    @Test
    fun `deadline passed`() {
        val currentDate = createDate(2000, 1, 4, second = 1)

        val habit = Habit.empty.copy(
            lastEditDate = createDate(2000, 1, 1),
            frequencyInDays = 3,
            timesToComplete = 5,
            doneDates = listOf(createDate(2000, 1, 2), createDate(2000, 1, 3), currentDate)
        )

        val result = completeHabitResultCalculator.getResult(habit, currentDate)

        assertEquals(CompletionUnreached(timesLeft = 4), result)
    }

    @Test
    fun `simple completion`() {
        val currentDate = createDate(2000, 1, 3)

        val habit = Habit.empty.copy(
            lastEditDate = createDate(2000, 1, 1),
            frequencyInDays = 3,
            timesToComplete = 2,
            doneDates = listOf(createDate(2000, 1, 2), currentDate)
        )

        val result = completeHabitResultCalculator.getResult(habit, currentDate)

        assertEquals(CompletionReached, result)
    }

    @Test
    fun `completion reached but keep doing`() {
        val currentDate = createDate(2000, 1, 3)

        val habit = Habit.empty.copy(
            lastEditDate = createDate(2000, 1, 1),
            frequencyInDays = 3,
            timesToComplete = 2,
            doneDates = listOf(createDate(2000, 1, 2), createDate(2000, 1, 2, 1), currentDate)
        )

        val result = completeHabitResultCalculator.getResult(habit, currentDate)

        assertEquals(CompletionReached, result)
    }

    @Test
    fun `completed a period but another started and done 1 time`() {
        val currentDate = createDate(2000, 1, 5)

        val habit = Habit.empty.copy(
            lastEditDate = createDate(2000, 1, 1),
            frequencyInDays = 3,
            timesToComplete = 2,
            doneDates = listOf(createDate(2000, 1, 2), createDate(2000, 1, 2, 1), currentDate)
        )

        val result = completeHabitResultCalculator.getResult(habit, currentDate)

        assertEquals(CompletionUnreached(timesLeft = 1), result)
    }

    private fun createDate(
        year: Int,
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0
    ): Date {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day, hour, minute, second)
        }
        return calendar.time
    }
}