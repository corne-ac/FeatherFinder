package com.ryanblignaut.featherfinder.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeConvert {

    fun fromStringToLong(date: String): Long {
        val formatter = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        val dateTime = formatter.parse(date) ?: return -1
        return dateTime.time
    }

    fun fromLongToString(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }
}