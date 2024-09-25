package com.squalec.liftingtracker.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
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

    actual fun formattedToDay(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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



    actual companion object {
        const val INIT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

        actual fun now(): CustomDate {
            val dateFormat = SimpleDateFormat(INIT_FORMAT, Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            return CustomDate(currentDate)
        }

        fun fromString(date: String): CustomDate {
            return CustomDate(date)
        }

        fun setByMonth(month: Month): CustomDate {
            val dateTime = LocalDateTime.parse(now().utcDate)

            // Create a new date with the same year and time, but with the month set to the provided Month
            val updatedDateTime = LocalDateTime(
                year = dateTime.year,
                month = month,
                dayOfMonth = 1,   // Set to the first day of the month
                hour = dateTime.hour,
                minute = dateTime.minute,
                second = dateTime.second,
                nanosecond = dateTime.nanosecond
            )

            // Return a new CustomDate with the updated date
            return CustomDate(updatedDateTime.toString())
        }

        fun setMonthFromInt(monthInt: Int): CustomDate {
            if (monthInt !in 1..12) {
                throw IllegalArgumentException("Invalid month. Must be between 1 and 12.")
            }
            val month = Month.values()[monthInt - 1]  // Convert Int to Month enum
            return setByMonth(month)
        }
    }
}
