package com.squalec.liftingtracker.utils

expect class CustomDate(utcDate: String) {
    fun defaultFormat(): String
    fun defaultFormatWithHours(): String
    fun formatUtcTime(): String
    fun displayFormat(): String
    companion object{
        fun now(): CustomDate
    }
}
