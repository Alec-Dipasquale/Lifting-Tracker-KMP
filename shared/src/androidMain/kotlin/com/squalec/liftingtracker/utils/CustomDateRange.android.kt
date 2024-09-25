package com.squalec.liftingtracker.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

actual class CustomDateRange actual constructor(
    private val customDate: CustomDate,
    private val rangeType: DateRange
) {
    actual fun getRange(): Pair<CustomDate, CustomDate> {
        val sdf = SimpleDateFormat(CustomDate.INIT_FORMAT, Locale.getDefault())
        val date = sdf.parse(customDate.utcDate) ?: throw IllegalArgumentException("Invalid date format")

        val calendar = Calendar.getInstance().apply {
            time = date
        }

        val startDate: Date
        val endDate: Date

        when (rangeType) {
            DateRange.DAY -> {
                // Start and end dates are the same for a single day
                startDate = calendar.time
                endDate = calendar.time
            }
            DateRange.WEEK -> {
                // Set calendar to the start of the week (Sunday or Monday depending on locale)
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                startDate = calendar.time

                // Move calendar to the end of the week
                calendar.add(Calendar.DAY_OF_WEEK, 6)
                endDate = calendar.time
            }
            DateRange.MONTH -> {
                // Set calendar to the first day of the month
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                startDate = calendar.time

                // Move calendar to the last day of the month
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                endDate = calendar.time
            }
        }

        // Return a pair of CustomDates representing the start and end of the range
        return Pair(
            CustomDate(sdf.format(startDate)),
            CustomDate(sdf.format(endDate))
        )
    }

    actual enum class DateRange {
        MONTH,
        WEEK,
        DAY
    }
}

