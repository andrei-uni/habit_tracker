package com.example.myapplication.data.datasources.local.database.converters

import androidx.room.TypeConverter
import java.util.Date

class DateListConverter {

    companion object {
        private const val SEPARATOR = ","
    }

    @TypeConverter
    fun fromDateList(dateList: List<Date>): String {
        val sb = StringBuilder()
        val size = dateList.size

        dateList.forEachIndexed { index, date ->
            sb.append(date.time)
            if (index != size - 1)
                sb.append(SEPARATOR)
        }

        return sb.toString()
    }

    @TypeConverter
    fun toDateList(data: String): List<Date> {
        val split = data.split(SEPARATOR)

        if (split.isEmpty() || split.first().isBlank())
            return emptyList()

        return split.map { Date(it.toLong()) }
    }
}