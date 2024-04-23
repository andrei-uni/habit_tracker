package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.models.CompleteHabitResult
import com.example.myapplication.domain.models.CompletionReached
import com.example.myapplication.domain.models.CompletionUnreached
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.repositories.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.floor

class CompleteHabitUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(habit: Habit): CompleteHabitResult = withContext(dispatcher) {
        val currentDate = Date()

        val updatedHabit = habitsRepository.completeHabit(habit.id, date = currentDate)

        getResult(updatedHabit, currentDate)
    }

    private fun getResult(habit: Habit, currentDate: Date): CompleteHabitResult {
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