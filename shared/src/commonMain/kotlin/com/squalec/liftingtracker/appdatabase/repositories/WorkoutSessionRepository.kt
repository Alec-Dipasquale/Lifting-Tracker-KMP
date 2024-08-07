package com.squalec.liftingtracker.appdatabase.repositories

import ExerciseDetailDao
import com.squalec.liftingtracker.appdatabase.dao.UserExerciseDao
import com.squalec.liftingtracker.appdatabase.dao.UserSetDao
import com.squalec.liftingtracker.appdatabase.dao.UserWorkoutSessionDao
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.models.UserWorkoutSession
import com.squalec.liftingtracker.utils.CustomDate
import kotlin.time.Duration

interface WorkoutSessionRepository {
    suspend fun getWorkoutSessionByDate(date: CustomDate): WorkoutSessionModel
    suspend fun getWorkoutSessionId(date: CustomDate): Long
    suspend fun saveWorkoutSession(workoutSessionModel: WorkoutSessionModel)
    suspend fun addExerciseToWorkoutSession()
    suspend fun removeExerciseFromWorkoutSession()
    suspend fun addSetToExercise()
    suspend fun removeSetFromExercise()
}

class WorkoutSessionRepositoryImpl(
    private val userWorkoutSessionDao: UserWorkoutSessionDao,
    private val userExerciseDao: UserExerciseDao,
    private val userSetDao: UserSetDao,
    private val exerciseDetailDao: ExerciseDetailDao
) : WorkoutSessionRepository {
    override suspend fun getWorkoutSessionByDate(date: CustomDate): WorkoutSessionModel {
        return userWorkoutSessionDao.getWorkoutSessionByDate(date.defaultFormat()).toWorkoutSessionModel(userExerciseDao, userSetDao, exerciseDetailDao)
    }

    override suspend fun getWorkoutSessionId(date: CustomDate): Long {
        return userWorkoutSessionDao.insertWorkoutSession(
            UserWorkoutSession(
                date = date.defaultFormat(),
                caloriesBurned = 0,
                duration = 0,
            )
        )
    }

    override suspend fun saveWorkoutSession(workoutSessionModel: WorkoutSessionModel) {
        val workoutSessionId = userWorkoutSessionDao.insertWorkoutSession(
            UserWorkoutSession(
                date = workoutSessionModel.date.defaultFormat(),
                caloriesBurned = workoutSessionModel.caloriesBurned,
                duration = workoutSessionModel.duration,
            )
        )
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

suspend fun UserWorkoutSession.toWorkoutSessionModel(
    exerciseDao: UserExerciseDao,
    setDao: UserSetDao,
    exerciseDetailDao: ExerciseDetailDao): WorkoutSessionModel {
    val exercises = exerciseDao.getUserExerciseDetailsForSession(id)
    return WorkoutSessionModel(
        date = CustomDate(date),
        exercises = exercises.map { exercise ->
            val sets = setDao.getUserExerciseSetsForWorkout(exercise.id)
            ExerciseSessionModel(
                exercise = exerciseDetailDao.getExerciseDetailsById(exercise.exerciseDetailsId),
                orderPosition = exercise.orderPosition,
                sets = sets.map { set ->
                    SetSessionModel(
                        orderPosition = set.orderPosition,
                        weight = set.weight,
                        reps = set.reps,
                    )
                }
            )
        }
    )
}

data class WorkoutSessionModel(
    val date: CustomDate,
    val exercises: List<ExerciseSessionModel> = listOf(ExerciseSessionModel(exercise = null, orderPosition = 0)),
    val duration: Long = 0,
    val caloriesBurned: Int = 0,
)

data class ExerciseSessionModel(
    val exercise: ExerciseDetails?,
    val orderPosition: Int,
    val sets: List<SetSessionModel> = listOf(SetSessionModel()),
)

data class SetSessionModel(
    val orderPosition: Int = 0,
    val weight: Float? = null,
    val reps: Int? = null,
)
