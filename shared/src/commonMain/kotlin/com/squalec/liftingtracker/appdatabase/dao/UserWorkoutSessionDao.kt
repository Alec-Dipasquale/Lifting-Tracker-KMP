package com.squalec.liftingtracker.appdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.squalec.liftingtracker.appdatabase.models.UserWorkoutSession

@Dao
interface UserWorkoutSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSession(workoutSession: UserWorkoutSession): Long
    @Query("SELECT * FROM user_workout_sessions")
    suspend fun getWorkoutSessions(): List<UserWorkoutSession>
    @Query("SELECT * FROM user_workout_sessions WHERE id = :sessionId")
    suspend fun getWorkoutSession(sessionId: Int): UserWorkoutSession
    @Query("SELECT * FROM user_workout_sessions WHERE date = :utcDate")
    suspend fun getWorkoutSessionByDate(utcDate: String): UserWorkoutSession
    @Query("SELECT * FROM user_workout_sessions WHERE date BETWEEN :startOfDay AND :endOfDay")
    suspend fun getWorkoutSessionsByDateRange(startOfDay: String, endOfDay: String): List<UserWorkoutSession>
    @Query("SELECT COUNT(*) FROM user_workout_sessions")
    suspend fun getWorkoutCount(): Int
}
