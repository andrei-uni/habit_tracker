package com.example.data.datasources.remote.habits_service

import com.example.data.datasources.remote.habits_service.api_objects.shared.HabitApi
import com.example.data.datasources.remote.habits_service.api_objects.shared.HabitDoneApi
import com.example.data.datasources.remote.habits_service.api_objects.shared.HabitUidApi
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface HabitsService {

    @GET("habit")
    suspend fun getHabits(): List<HabitApi>

    @PUT("habit")
    suspend fun addHabit(@Body habitApi: HabitApi): HabitUidApi

    @PUT("habit")
    suspend fun updateHabit(@Body habitApi: HabitApi): HabitUidApi

    @POST("habit_done")
    suspend fun doHabit(@Body habitDoneApi: HabitDoneApi)

//    @HTTP(method = "DELETE", path = "habit", hasBody = true)
//    suspend fun deleteHabit(@Body habitUidApi: HabitUidApi)
}