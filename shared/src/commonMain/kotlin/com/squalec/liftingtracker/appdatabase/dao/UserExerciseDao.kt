package com.squalec.liftingtracker.appdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.squalec.liftingtracker.appdatabase.models.UserExercise

@Dao
interface UserExerciseDao {
    @Insert
    suspend fun insertUserExerciseDetails(userExerciseDetails: UserExercise)
    @Query("SELECT * FROM user_exercise")
    suspend fun getUserExerciseDetails(): List<UserExercise>
    @Query("SELECT * FROM user_exercise WHERE workout_session_id = :sessionId")
    suspend fun getUserExerciseDetailsForSession(sessionId: Int): List<UserExercise>
    @Query("SELECT * FROM user_exercise WHERE id = :exerciseId")
    suspend fun getUserExerciseDetails(exerciseId: Int): UserExercise
    @Insert
    suspend fun insertAllUserExerciseDetails(exercises: List<UserExercise>)
}