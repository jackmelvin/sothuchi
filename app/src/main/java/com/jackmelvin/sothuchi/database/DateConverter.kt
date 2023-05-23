package com.jackmelvin.sothuchi.database

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @TypeConverter
    fun toCalendar(timestamp: Long?): Calendar? {
        val calendar = Calendar.getInstance()
        return timestamp?.let {
            calendar.timeInMillis = it
            calendar
        }
    }

    @TypeConverter
    fun fromCalendar(calendar: Calendar?): Long? {
        return calendar?.time?.time
    }
}