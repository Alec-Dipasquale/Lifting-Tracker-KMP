package com.squalec.liftingtracker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {
    // Implementation details...
}

// Helper extension
internal fun <T> Flow<T>.wrap(): CFlow<T> = CFlow(this)

// Remove when Kotlin's Closeable is supported in K/N
interface Closeable {
    fun close()
}