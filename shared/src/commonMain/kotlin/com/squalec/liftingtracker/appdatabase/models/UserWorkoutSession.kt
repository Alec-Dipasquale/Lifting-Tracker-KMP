package com.squalec.liftingtracker.appdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "user_workout_sessions")
data class  UserWorkoutSession(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "workout_name", defaultValue = "") val workoutName: String = "",
    @ColumnInfo(name = "date") val date: String = "",
    @ColumnInfo(name = "duration") val duration: Long = 0,
    @ColumnInfo(name = "calories_burned") val caloriesBurned: Int = 0,
)