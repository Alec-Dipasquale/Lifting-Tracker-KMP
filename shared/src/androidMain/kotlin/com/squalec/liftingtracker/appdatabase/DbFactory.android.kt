package com.squalec.liftingtracker.appdatabase

import android.content.Context
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
}