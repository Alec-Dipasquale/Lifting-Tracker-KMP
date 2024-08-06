package com.squalec.liftingtracker.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

actual class CustomDate actual constructor(val utcDate: String) {

    init {
        // Validate and parse the input date string to ensure it's in the correct format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd:HH:mm:ss", Locale.getDefault())
        try {
            dateFormat.parse(utcDate)
        } catch (e: ParseException) {
            throw IllegalArgumentException("Invalid date format, expected format is yyyy-MM-dd:HH:mm:ss")
        }
    }

    actual fun defaultFormat(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(sdf.parse(utcDate)!!)    }

    actual fun defaultFormatWithHours(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd:H", Locale.getDefault())
        return sdf.format(sdf.parse(utcDate)!!)
    }

    actual fun formatUtcTime(): String {
        TODO("Not yet implemented")
    }
}