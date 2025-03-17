package com.squalec.liftingtracker.appdatabase

import timber.log.Timber

actual class Logs actual constructor() {

    private val logTag = "MY_APP_LOG"

    actual fun log(message: String) {
        Timber.tag(logTag).log(1, message, null, null)
    }

    actual fun error(message: String) {
        Timber.tag(logTag).e(message, null, null, null)
    }

    actual fun debug(message: String) {
        Timber.tag(logTag).d(message, null, null, null)
    }
}