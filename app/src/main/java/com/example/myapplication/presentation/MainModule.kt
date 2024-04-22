package com.example.myapplication.presentation

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.datasources.local.database.AppDatabase
import com.example.myapplication.data.datasources.remote.habits_service.HabitsService
import com.example.myapplication.data.datasources.remote.habits_service.HabitsServiceInterceptor
import com.example.myapplication.data.repositories.CompoundHabitsRepository
import com.example.myapplication.data.repositories.LocalHabitsRepositoryImpl
import com.example.myapplication.data.repositories.RemoteHabitsRepositoryImpl
import com.example.myapplication.domain.repositories.HabitsRepository
import com.example.myapplication.domain.repositories.LocalHabitsRepository
import com.example.myapplication.domain.repositories.RemoteHabitsRepository
import com.example.myapplication.domain.usecases.AddHabitUseCase
import com.example.myapplication.domain.usecases.GetHabitsUseCase
import com.example.myapplication.domain.usecases.UpdateHabitUseCase
import com.example.myapplication.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideAddHabitUseCase(habitsRepository: HabitsRepository): AddHabitUseCase {
        return AddHabitUseCase(habitsRepository, Dispatchers.IO)
    }

    @Provides
    fun provideUpdateHabitUseCase(habitsRepository: HabitsRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(habitsRepository, Dispatchers.IO)
    }

    @Provides
    fun provideGetHabitsUseCase(habitsRepository: HabitsRepository): GetHabitsUseCase {
        return GetHabitsUseCase(habitsRepository, Dispatchers.IO)
    }

    @Singleton
    @Provides
    fun provideHabitsRepository(
        remoteHabitsRepository: RemoteHabitsRepository,
        localHabitsRepository: LocalHabitsRepository,
    ): HabitsRepository {
        return CompoundHabitsRepository(remoteHabitsRepository, localHabitsRepository)
    }

    @Singleton
    @Provides
    fun provideRemoteHabitsRepository(habitsService: HabitsService): RemoteHabitsRepository {
        return RemoteHabitsRepositoryImpl(habitsService)
    }

    @Singleton
    @Provides
    fun provideLocalHabitsRepository(appDatabase: AppDatabase): LocalHabitsRepository {
        return LocalHabitsRepositoryImpl(appDatabase)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideHabitsService(retrofit: Retrofit): HabitsService {
        return retrofit.create(HabitsService::class.java)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(
                Json.asConverterFactory("application/json".toMediaType())
            )
            .baseUrl(Constants.HABITS_SERVICE_BASE_URL)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(HabitsServiceInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
}