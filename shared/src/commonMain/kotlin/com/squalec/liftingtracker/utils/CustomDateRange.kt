package com.squalec.liftingtracker.utils

expect class CustomDateRange(customDate: CustomDate, rangeType: CustomDateRange.DateRange) {
    fun getRange(): Pair<CustomDate, CustomDate>

        enum class DateRange {
            MONTH,
            WEEK,
            DAY
        }

}

