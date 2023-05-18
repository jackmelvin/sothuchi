package com.jackmelvin.sothuchi.converter

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
//    @TypeConverter
//    fun toDate(timestamp: Long?): Date? {
//        return if (timestamp == null) null else Date(timestamp)
//    }
//
//    @TypeConverter
//    fun toTimestamp(date: Date?): Long? {
//        return date?.time
//    }
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