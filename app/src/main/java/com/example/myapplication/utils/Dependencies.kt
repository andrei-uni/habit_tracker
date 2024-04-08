package com.example.myapplication.utils

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.datasources.local.database.AppDatabase

object Dependencies {

    private lateinit var applicationContext: Context

    val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .allowMainThreadQueries()
            .build()
    }

    fun init(applicationContext: Context) {
        this.applicationContext = applicationContext
    }
}