package com.example.myapplication.utils

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
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object Dependencies {

    private lateinit var applicationContext: Context

    val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .build()
    }

    val habitsService: HabitsService by lazy {
//        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }

        val client = OkHttpClient().newBuilder()
            .addInterceptor(HabitsServiceInterceptor())
//            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(
                Json.asConverterFactory("application/json".toMediaType())
            )
            .baseUrl(Constants.HABITS_SERVICE_BASE_URL)
            .build()

        retrofit.create(HabitsService::class.java)
    }

    val remoteHabitsRepository: RemoteHabitsRepository by lazy {
        RemoteHabitsRepositoryImpl()
    }

    val localHabitsRepository: LocalHabitsRepository by lazy {
        LocalHabitsRepositoryImpl()
    }

    val habitsRepository: HabitsRepository by lazy {
        CompoundHabitsRepository()
    }

    fun init(applicationContext: Context) {
        this.applicationContext = applicationContext

//        try {
//            (remoteHabitsRepository as RemoteHabitsRepositoryImpl).deleteAll()
//        } catch (_: Exception) {}
    }
}