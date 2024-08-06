package com.squalec.liftingtracker.appdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.squalec.liftingtracker.appdatabase.models.UserSet

@Dao
interface UserSetDao {
    @Insert
    suspend fun insertUserExerciseSet(userExerciseSet: UserSet)
    @Query("SELECT * FROM user_sets")
    suspend fun getUserExerciseSets(): List<UserSet>
    @Query("SELECT * FROM user_sets WHERE exercise_id = :exerciseId")
    suspend fun getUserExerciseSetsForWorkout(exerciseId: String): List<UserSet>
}