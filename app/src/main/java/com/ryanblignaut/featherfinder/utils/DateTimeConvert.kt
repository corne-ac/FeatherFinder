package com.ryanblignaut.featherfinder.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeConvert {

    fun fromStringToLong(date: String): Long {
        val formatter = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        try {
            val dateTime = formatter.parse(date) ?: return -1
            return dateTime.time
        } catch (e: Exception) {
            return -1
        }
    }

    fun fromLongToString(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }
}