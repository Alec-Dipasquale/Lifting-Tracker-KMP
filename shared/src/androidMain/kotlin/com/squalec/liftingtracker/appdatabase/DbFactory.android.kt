package com.squalec.liftingtracker.appdatabase

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.getKoin

actual object DBFactory {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    actual fun createDatabase(): AppDatabase {
        val context: Context = getKoin().get()
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "exercise-db"
            ).build()
            INSTANCE = instance
            instance
        }
    }

    actual suspend fun populateExerciseDetailsDatabase() {
        val context: Context = getKoin().get()
        val jsonString = uploadJsonToDatabase(context, "exercises.json")
        val exercises = parseExercises(jsonString)
        Log.d("[Database123]", "Exercises: $exercises")
        val db = DBFactory.createDatabase()
        db.exerciseDao().insertExercises(*exercises.toTypedArray())
    }

    private fun uploadJsonToDatabase(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}