package com.squalec.liftingtracker.appdatabase

import Converters
import ExerciseDetailDao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails

@Database(entities = [ExerciseDetails::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(), DB {
    abstract fun exerciseDao(): ExerciseDetailDao
    override fun clearAllTables() {
        super.clearAllTables()
    }
}

internal const val dbFileName = "app_room_db.db"

interface DB{
    fun clearAllTables() {}
}