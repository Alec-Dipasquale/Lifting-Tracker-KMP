package com.squalec.liftingtracker.appdatabase

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.getKoin


actual object DBFactory : KoinComponent {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    actual fun createDatabase(): AppDatabase {
        val context: Context = getKoin().get()
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                dbFileName
            )
                .build()
            INSTANCE = instance
            instance
        }
    }

    actual suspend fun populateExerciseDetailsDatabase() {
        val context: Context = getKoin().get()
        val jsonString = uploadJsonToDatabase(context, "exercises.json")
        val exercises = parseExercises(jsonString)
        Logs().log("Adding exercises to database")
        val db = createDatabase()
        db.exerciseDao().insertExercises(*exercises.toTypedArray())
        Logs().log("Exercises added to database")
    }

    private fun uploadJsonToDatabase(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}