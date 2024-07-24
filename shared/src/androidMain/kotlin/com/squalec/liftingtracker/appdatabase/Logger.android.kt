package com.squalec.liftingtracker.appdatabase

import timber.log.Timber

actual class Logs actual constructor() {
    actual fun log(message: String) {
        Timber.log(1, message, null, null)
    }

    actual fun error(message: String) {
        Timber.e(message, null, null, null)
    }

    actual fun debug(message: String) {
        Timber.d(message, null, null, null)
    }
}