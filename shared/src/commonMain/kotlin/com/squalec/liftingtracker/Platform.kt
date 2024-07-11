package com.squalec.liftingtracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform