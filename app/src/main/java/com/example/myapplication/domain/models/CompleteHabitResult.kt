package com.example.myapplication.domain.models

sealed class CompleteHabitResult

data object CompletionReached : CompleteHabitResult()

data class CompletionUnreached(val timesLeft: Int) : CompleteHabitResult()