package com.squalec.liftingtracker.appdatabase

import Converters
import ExerciseDetailDao
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.squalec.liftingtracker.appdatabase.dao.UserExerciseDao
import com.squalec.liftingtracker.appdatabase.dao.UserSetDao
import com.squalec.liftingtracker.appdatabase.dao.UserWorkoutSessionDao
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.models.UserExercise
import com.squalec.liftingtracker.appdatabase.models.UserSet
import com.squalec.liftingtracker.appdatabase.models.UserWorkoutSession

@Database(
    entities = [
        ExerciseDetails::class,
        UserWorkoutSession::class,
        UserExercise::class,
        UserSet::class
    ],
    version = 3,

//    autoMigrations = [
////        AutoMigration (from = 2, to = 3)
//    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun exerciseDao(): ExerciseDetailDao
    abstract fun userWorkoutSessionDao(): UserWorkoutSessionDao
    abstract fun userExerciseDao(): UserExerciseDao
    abstract fun userSetDao(): UserSetDao
    override fun clearAllTables() {
        super.clearAllTables()
    }
}

internal const val dbFileName = "app_room_db.db"

interface DB {
    fun clearAllTables() {}
}
