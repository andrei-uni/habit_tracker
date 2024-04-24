package com.example.data.datasources.remote.habits_service

import com.example.constants.Constants
import okhttp3.Interceptor
import okhttp3.Response

class HabitsServiceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader("Authorization", Constants.HABITS_SERVICE_TOKEN)
            .build()

        return chain.proceed(newRequest)
    }
}