package com.example.myapplication.utils

import com.example.domain.usecases.CompleteHabitUseCase
import com.example.domain.usecases.GetHabitsUseCase
import com.example.myapplication.presentation.habits_list_viewmodel.HabitsListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object HabitsListViewModelModule {

    @ActivityRetainedScoped
    @Provides
    fun provideHabitsListViewModel(
        getHabitsUseCase: GetHabitsUseCase,
        completeHabitUseCase: CompleteHabitUseCase,
    ): HabitsListViewModel {
        return HabitsListViewModel(getHabitsUseCase, completeHabitUseCase);
    }
}