package com.squalec.liftingtracker.utils

import java.text.SimpleDateFormat

actual class CustomDate actual constructor(val utcDate: String) {
    actual fun defaultFormatUtcDate(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(utcDate)
    }

    actual fun formatUtcDateTime(): String {
        TODO("Not yet implemented")
    }

    actual fun formatUtcTime(): String {
        TODO("Not yet implemented")
    }
}