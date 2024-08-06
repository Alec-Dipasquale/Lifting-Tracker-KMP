package com.squalec.liftingtracker.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual class CustomDate actual constructor(val utcDate: String) {

    init {
        // Validate and parse the input date string to ensure it's in the correct format
        val dateFormat = SimpleDateFormat(INIT_FORMAT, Locale.getDefault())
        try {
            dateFormat.parse(utcDate)
        } catch (e: ParseException) {
            throw IllegalArgumentException("Invalid date format, expected format is $INIT_FORMAT")
        }
    }

    actual fun defaultFormat(): String {
        val sdf = SimpleDateFormat(INIT_FORMAT, Locale.getDefault())
        return sdf.format(sdf.parse(utcDate)!!)
    }

    actual fun defaultFormatWithHours(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd:HH", Locale.getDefault())
        return sdf.format(sdf.parse(utcDate)!!)
    }

    actual fun formatUtcTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(sdf.parse(utcDate)!!)
    }

    actual fun displayFormat(): String {
        val inputFormat = SimpleDateFormat(INIT_FORMAT, Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val parsedDate = inputFormat.parse(utcDate)
        return outputFormat.format(parsedDate!!)
    }

    companion object {
        const val INIT_FORMAT = "yyyy-MM-dd:HH:mm:ss"

        fun now(): CustomDate {
            val dateFormat = SimpleDateFormat(INIT_FORMAT, Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            return CustomDate(currentDate)
        }

        fun fromString(date: String): CustomDate {
            return CustomDate(date)
        }
    }
}
