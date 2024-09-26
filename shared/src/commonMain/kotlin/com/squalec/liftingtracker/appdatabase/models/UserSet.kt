package com.squalec.liftingtracker.appdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "user_sets",
    foreignKeys = [
        ForeignKey(
            entity = UserExercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE // Optional: Specify what happens on delete
        )
    ],
    indices = [Index(value = ["exercise_id"])] // Ensure the foreign key column is indexed
)
data class UserSet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "order_position") val orderPosition: Int,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "reps") val reps: Int,
    @ColumnInfo(name = "exercise_id") val exerciseId: String
)