package com.squalec.liftingtracker.appdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(
    tableName = "user_exercise",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseDetails::class,
            parentColumns = ["id"],
            childColumns = ["exercise_details_id"],
            onDelete = ForeignKey.CASCADE // Optional: Specify what happens on delete
        ),
        ForeignKey(
            entity = UserWorkoutSession::class,
            parentColumns = ["id"],
            childColumns = ["workout_session_id"],
            onDelete = ForeignKey.CASCADE // Optional: Specify what happens on delete
        )
    ],
    indices = [
        Index(value = ["exercise_details_id"]),
        Index(value = ["workout_session_id"])
    ] // Ensure the foreign key column is indexed
)
data class UserExercise(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "order_position") val orderPosition: Int,
    @ColumnInfo(name = "exercise_details_id") val exerciseDetailsId: String,
    @ColumnInfo(name = "workout_session_id") val workoutSessionId: String
)