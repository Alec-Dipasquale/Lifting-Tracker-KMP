package com.squalec.liftingtracker.appdatabase

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.getKoin
import java.util.concurrent.Executors


actual object DBFactory : KoinComponent {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    actual fun createDatabase(): AppDatabase {
        val context: Context = getKoin().get()
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        dbFileName,
                    )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        db.execSQL("PRAGMA foreign_keys = ON;") // Enable foreign key constraints
                    }
                })
                .fallbackToDestructiveMigration(false)

//                .setQueryCallback({ sqlQuery, bindArgs ->
//                    Logs().debug("[SQL Query] Query: $sqlQuery Args: $bindArgs")
//                }, Runnable::run) // Runs on the current thread
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
        val insertResults = db.exerciseDao().insertExercises(*exercises.toTypedArray())

        if (insertResults.all { it == -1L }) {
            Logs().log("All exercises already exist in the database; no new rows added.")
        } else {
            Logs().log("${insertResults.count { it != -1L }} new exercises added to the database.")
        }
    }


    private fun uploadJsonToDatabase(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create a new table with the correct schema
            database.execSQL("""
            CREATE TABLE user_exercise_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                order_position INTEGER NOT NULL,
                exercise_details_id TEXT NOT NULL,
                workout_session_id TEXT NOT NULL
            )
        """)

            // Copy the data from the old table to the new table
            database.execSQL("""
            INSERT INTO user_exercise_new (order_position, exercise_details_id, workout_session_id)
            SELECT order_position, exercise_details_id, workout_session_id FROM user_exercise
        """)

            // Remove the old table
            database.execSQL("DROP TABLE user_exercise")

            // Rename the new table to the old table name
            database.execSQL("ALTER TABLE user_exercise_new RENAME TO user_exercise")
        }
    }

}