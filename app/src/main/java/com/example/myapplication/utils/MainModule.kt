package com.example.myapplication.utils

import android.content.Context
import androidx.room.Room
import com.example.constants.Constants
import com.example.data.datasources.local.database.AppDatabase
import com.example.data.datasources.remote.habits_service.HabitsService
import com.example.data.datasources.remote.habits_service.HabitsServiceInterceptor
import com.example.data.repositories.HabitsRepositoryImpl
import com.example.domain.repositories.HabitsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
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
    fun provideUseCaseCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    fun provideHabitsRepository(
        habitsService: HabitsService,
        appDatabase: AppDatabase,
    ): HabitsRepository {
        return HabitsRepositoryImpl(habitsService, appDatabase)
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