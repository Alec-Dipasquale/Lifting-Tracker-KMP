package com.squalec.liftingtracker.appdatabase.repositories

import ExerciseDetailDao
import com.squalec.liftingtracker.appdatabase.Logs
import com.squalec.liftingtracker.appdatabase.dao.UserExerciseDao
import com.squalec.liftingtracker.appdatabase.dao.UserSetDao
import com.squalec.liftingtracker.appdatabase.dao.UserWorkoutSessionDao
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.models.UserExercise
import com.squalec.liftingtracker.appdatabase.models.UserSet
import com.squalec.liftingtracker.appdatabase.models.UserWorkoutSession
import com.squalec.liftingtracker.utils.CustomDate
import com.squalec.liftingtracker.utils.CustomDateRange
import kotlin.time.Duration

interface WorkoutSessionRepository {
    suspend fun getWorkoutSessionByDate(date: CustomDate): WorkoutSessionModel
    suspend fun getWorkoutSessionByRange(dateRange: CustomDateRange): List<WorkoutSessionModel>
    suspend fun getWorkoutSessionId(date: CustomDate): Long
    suspend fun getWorkoutSessionById(workoutId: String): WorkoutSessionModel?
    suspend fun saveWorkoutSession(workoutSessionModel: WorkoutSessionModel)
    suspend fun addExerciseToWorkoutSession()
    suspend fun removeExerciseFromWorkoutSession()
    suspend fun addSetToExercise()
    suspend fun removeSetFromExercise()
    suspend fun getWorkoutCount(): Int
}

class WorkoutSessionRepositoryImpl(
    private val userWorkoutSessionDao: UserWorkoutSessionDao,
    private val userExerciseDao: UserExerciseDao,
    private val userSetDao: UserSetDao,
    private val exerciseDetailDao: ExerciseDetailDao,
) : WorkoutSessionRepository {
    override suspend fun getWorkoutSessionByDate(date: CustomDate): WorkoutSessionModel {
        try {
            return userWorkoutSessionDao.getWorkoutSessionByDate(date.defaultFormat())
                .toWorkoutSessionModel(userExerciseDao, userSetDao, exerciseDetailDao)
        } catch (e: Exception) {
            Logs().error("Error getting workout session by date: ${e.message}")
            return WorkoutSessionModel(date = date)
        }
    }

    override suspend fun getWorkoutSessionByRange(dateRange: CustomDateRange): List<WorkoutSessionModel> {
        try {
            val (startOfDay, endOfDay) = dateRange.getRange()
            return userWorkoutSessionDao.getWorkoutSessionsByDateRange(
                startOfDay.defaultFormat(),
                endOfDay.defaultFormat()
            ).map { it.toWorkoutSessionModel(userExerciseDao, userSetDao, exerciseDetailDao) }
        } catch (e: Exception) {
            Logs().error("Error getting workout sessions by date range: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun getWorkoutSessionId(date: CustomDate): Long {
        try {
            return userWorkoutSessionDao.insertWorkoutSession(
                UserWorkoutSession(
                    date = date.defaultFormat(),
                    caloriesBurned = 0,
                    duration = 0,
                )
            )
        } catch (e: Exception) {
            Logs().error("Error getting workout session ID: ${e.message}")
            return -1
        }
    }

    override suspend fun saveWorkoutSession(workoutSessionModel: WorkoutSessionModel) {

        try {
            Logs().debug("Saving workout session: ${workoutSessionModel.workoutName}, " +
                    "exercises: ${workoutSessionModel.exercises.joinToString { it.exercise?.name ?: ("null") }}"
            )


            // Check for exercises
            if (workoutSessionModel.exercises.any { it.exercise?.id.isNullOrBlank() }) {
                throw IllegalArgumentException("All exercises must have a valid exercise ID")
            }

            // Check if exercises is empty
            if (workoutSessionModel.exercises.isEmpty()) {
                throw IllegalArgumentException("Workout must have at least one exercise")
            }

            // Check for sets
            if (workoutSessionModel.exercises.any { it.sets.isEmpty() }) {
                throw IllegalArgumentException("All exercises must have at least one set")
            }

            // Insert workout session
            val userWorkoutSession = UserWorkoutSession(
                workoutName = workoutSessionModel.workoutName,
                date = workoutSessionModel.date.defaultFormat(),
                caloriesBurned = workoutSessionModel.caloriesBurned,
                duration = workoutSessionModel.duration,
            )
            val workoutId = userWorkoutSessionDao.insertWorkoutSession(userWorkoutSession)


            // Insert exercises
            userExerciseDao.insertAllUserExerciseDetails(
                workoutSessionModel.exercises.map { it.toUserExercise(workoutId.toString()) }
            )

            // Insert sets
            userSetDao.insertAllUserExerciseSets(
                workoutSessionModel.exercises.flatMap { exercise ->
                    val exerciseId =
                        userExerciseDao.getUserExerciseDetailsForSession(workoutId.toInt())
                            .find { it.orderPosition == exercise.orderPosition }?.id.toString()
                    exercise.sets.map { it.toUserSet(exerciseId) }
                }
            )

        } catch (e: Exception) {
            Logs().error("Error saving workout session: ${e.message}")
        }
    }

    override suspend fun getWorkoutSessionById(workoutId: String): WorkoutSessionModel? {
        try {
            return userWorkoutSessionDao.getWorkoutSession(workoutId.toInt())
                .toWorkoutSessionModel(userExerciseDao, userSetDao, exerciseDetailDao)
        } catch (e: Exception) {
            Logs().error("Error getting workout session by ID: ${e.message}")
            return null
        }
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

    override suspend fun getWorkoutCount(): Int {
        try {
            return userWorkoutSessionDao.getWorkoutCount()
        }   catch (e: Exception) {
            Logs().error("Error getting workout count: ${e.message}")
            return -1
        }
    }


    private fun ExerciseSessionModel.toUserExercise(sessionId: String): UserExercise {
        Logs().debug("Converting exercise session model to user exercise: $this")
        return UserExercise(
            orderPosition = orderPosition,
            exerciseDetailsId = exercise?.id ?: "",
            workoutSessionId = sessionId
        )
    }

    private fun SetSessionModel.toUserSet(exerciseId: String): UserSet {
        Logs().debug("Converting set session model to user set: $this")
        return UserSet(
            orderPosition = orderPosition,
            weight = weight ?: 0f,
            reps = reps ?: 0,
            exerciseId = exerciseId,
        )
    }

    private suspend fun UserWorkoutSession.toWorkoutSessionModel(
        exerciseDao: UserExerciseDao,
        setDao: UserSetDao,
        exerciseDetailDao: ExerciseDetailDao
    ): WorkoutSessionModel {
        val metricType = when (metricType) {
            "LB" -> WeightMetricTypes.LB
            "KG" -> WeightMetricTypes.KG
            else -> WeightMetricTypes.LB
        }
        val exercises = exerciseDao.getUserExerciseDetailsForSession(id)
        return WorkoutSessionModel(
            workoutId = id.toString(),
            workoutName = workoutName,
            date = CustomDate(date),
            metricType = metricType,
            exercises = exercises.map { exercise ->
                val sets = setDao.getUserExerciseSetsForWorkout(exercise.id.toString())
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
}


data class WorkoutSessionModel(
    val workoutId: String? = null,
    val workoutName: String = "Workout",
    val date: CustomDate,
    val exercises: List<ExerciseSessionModel> = listOf(
        ExerciseSessionModel(
            exercise = null,
            orderPosition = 0
        )
    ),
    val metricType:WeightMetricTypes = WeightMetricTypes.LB,
    val duration: Long = 0,
    val caloriesBurned: Int = 0,
)

enum class WeightMetricTypes {
    LB,
    KG,
}

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
