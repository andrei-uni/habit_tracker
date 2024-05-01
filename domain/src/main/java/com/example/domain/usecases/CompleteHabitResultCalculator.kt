package com.example.domain.usecases

import com.example.domain.models.CompleteHabitResult
import com.example.domain.models.CompletionReached
import com.example.domain.models.CompletionUnreached
import com.example.domain.models.Habit
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.floor

internal object CompleteHabitResultCalculator {

     fun getResult(habit: Habit, currentDate: Date): CompleteHabitResult {
        val currentPeriodStartDate = getCurrentPeriodStartDate(
            startDate = habit.lastEditDate,
            currentDate = currentDate,
            periodLengthInDays = habit.frequencyInDays.toLong(),
        )

        val timesDoneInCurrentPeriod = habit.doneDates.count { it.after(currentPeriodStartDate) }

        val timesToCompleteLeft = habit.timesToComplete - timesDoneInCurrentPeriod

        if (timesToCompleteLeft > 0) {
            return CompletionUnreached(timesLeft = timesToCompleteLeft)
        }
        return CompletionReached
    }

    private fun getCurrentPeriodStartDate(
        startDate: Date,
        currentDate: Date,
        periodLengthInDays: Long,
    ): Date {
        val daysPassed = getDifferenceInDaysBetweenDates(startDate, currentDate).toDouble()

        val periodsPassed = floor(daysPassed / periodLengthInDays).toLong()

        val periodLengthInMillis = TimeUnit.DAYS.toMillis(periodLengthInDays)

        val currentPeriodStartDateMillis = startDate.time + periodsPassed * periodLengthInMillis

        return Date(currentPeriodStartDateMillis)
    }

    private fun getDifferenceInDaysBetweenDates(date1: Date, date2: Date): Long {
        val diffMillis = abs(date1.time - date2.time)
        return TimeUnit.MILLISECONDS.toDays(diffMillis)
    }
}