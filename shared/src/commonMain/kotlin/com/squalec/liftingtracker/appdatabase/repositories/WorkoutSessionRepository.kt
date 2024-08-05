package com.squalec.liftingtracker.appdatabase.repositories

import com.squalec.liftingtracker.appdatabase.dao.UserExerciseDao
import com.squalec.liftingtracker.appdatabase.dao.UserSetDao
import com.squalec.liftingtracker.appdatabase.dao.UserWorkoutSessionDao
import com.squalec.liftingtracker.appdatabase.models.UserWorkoutSession
import com.squalec.liftingtracker.utils.CustomDate

internal interface WorkoutSessionRepository {
    suspend fun loadWorkoutSessionByDate(date: CustomDate): UserWorkoutSession
    suspend fun getWorkoutSessionId(date: CustomDate): Long
    suspend fun saveWorkoutSession()
    suspend fun addExerciseToWorkoutSession()
    suspend fun removeExerciseFromWorkoutSession()
    suspend fun addSetToExercise()
    suspend fun removeSetFromExercise()
}

class WorkoutSessionRepositoryImpl(
    private val userWorkoutSessionDao: UserWorkoutSessionDao,
    private val userExerciseDao: UserExerciseDao,
    private val userSetDao: UserSetDao
) : WorkoutSessionRepository {
    override suspend fun loadWorkoutSessionByDate(date: CustomDate): UserWorkoutSession {
        return userWorkoutSessionDao.getWorkoutSessionByDate(date.defaultFormatUtcDate())
    }

    override suspend fun getWorkoutSessionId(date: CustomDate): Long {
        return userWorkoutSessionDao.insertWorkoutSession(
            UserWorkoutSession(
                date = date.defaultFormatUtcDate(),
                caloriesBurned = 0,
                duration = 0,
            )
        )
    }

    override suspend fun saveWorkoutSession() {
        // Save workout session
    }

    override suspend fun addExerciseToWorkoutSession() {
        // Add exercise to workout session
    }

    override suspend fun removeExerciseFromWorkoutSession() {
        // Remove exercise from workout session
    }

    override suspend fun addSetToExercise() {
        // Add set to exercise
    }

    override suspend fun removeSetFromExercise() {
        // Remove set from exercise
    }
}
