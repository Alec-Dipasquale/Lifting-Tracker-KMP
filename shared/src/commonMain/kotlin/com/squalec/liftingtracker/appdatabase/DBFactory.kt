package com.squalec.liftingtracker.appdatabase

expect object DBFactory {
    fun createDatabase(): AppDatabase
    suspend fun populateExerciseDetailsDatabase()
}