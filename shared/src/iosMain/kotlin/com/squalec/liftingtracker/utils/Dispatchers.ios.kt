package com.squalec.liftingtracker.utils

import kotlinx.coroutines.*
import platform.darwin.*

actual val Dispatchers.IO: CoroutineDispatcher
    get() = IODispatcher

@OptIn(InternalCoroutinesApi::class)
private object IODispatcher : CoroutineDispatcher(), Delay {
    // Implementation details...
}