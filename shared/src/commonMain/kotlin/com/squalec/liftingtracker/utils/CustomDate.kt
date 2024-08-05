package com.squalec.liftingtracker.utils

expect class CustomDate(utcDate: String) {
    fun defaultFormatUtcDate(): String
    fun formatUtcDateTime(): String
    fun formatUtcTime(): String
}
