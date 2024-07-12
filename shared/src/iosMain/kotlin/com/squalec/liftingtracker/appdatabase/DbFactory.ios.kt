package com.squalec.liftingtracker.appdatabase

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.withContext
import platform.Foundation.NSBundle
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile
import kotlin.concurrent.Volatile

actual object DBFactory {

    private const val dbFileName = "/exercise-db"
    @OptIn(InternalCoroutinesApi::class)
    private val lock = SynchronizedObject()

    @Volatile
    private var INSTANCE: AppDatabase? = null

    @OptIn(InternalCoroutinesApi::class)
    actual fun createDatabase(): AppDatabase {
        val dbFile = NSHomeDirectory() + dbFileName
        return INSTANCE ?: synchronized(lock = lock, block =  {
            val instance = Room.databaseBuilder<AppDatabase>(dbFile, factory = {
                AppDatabase::class.instantiateImpl()
            })
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
            INSTANCE = instance
            instance
        })
    }

    actual suspend fun populateExerciseDetailsDatabase() {
        val jsonString = uploadJsonToDatabase("exercises.json")
        if (jsonString != null) {
            val exercises = parseExercises(jsonString)
            val db = createDatabase() // Obtain your SQLDelight database instance here
            withContext(Dispatchers.Default) {
                db.exerciseDao().insertExercises(*exercises.toTypedArray())
            }
        } else {
            println("Error: Could not load exercises.json from bundle.")
        }
    }


    @OptIn(ExperimentalForeignApi::class)
    fun uploadJsonToDatabase(fileName: String): String? {
        val path = NSBundle.mainBundle.pathForResource(name = fileName, ofType = null)
        return if (path != null) {
            NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = null)
        } else {
            null
        }
    }
}