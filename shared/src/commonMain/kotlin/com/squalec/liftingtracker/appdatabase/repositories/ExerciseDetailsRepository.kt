package com.squalec.workoutmodule.data.repositories

import ExerciseDetailDao
import com.squalec.liftingtracker.appdatabase.Logs
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import org.koin.core.logger.Logger

internal interface ExerciseDetailsRepository {
    suspend fun getAllExerciseDetails(): List<ExerciseDetails>

    suspend fun insertAllExerciseDetails(exerciseDetails: List<ExerciseDetails>)

    suspend fun muscleNames(): List<String>

    suspend fun searchExercises(search: String): List<ExerciseDetails>

    suspend fun searchExercisesWithFilters(
        search: String? = null,
        muscles: List<String>? = null,
        equipment: String? = null,
        level: String? = null,
        force: String? = null,
        mechanic: String? = null,
        category: String? = null
    ): List<ExerciseDetails>

}
// todo koin injection for dao
class ExerciseDetailsRepositoryImpl(
    private val exerciseDetailsDao: ExerciseDetailDao
) : ExerciseDetailsRepository {
    override suspend fun getAllExerciseDetails(): List<ExerciseDetails> {
        return exerciseDetailsDao.getAllExercises()
    }

    override suspend fun insertAllExerciseDetails(exerciseDetails: List<ExerciseDetails>) {
        exerciseDetailsDao.insertAll(exerciseDetails)
    }

    override suspend fun muscleNames(): List<String> {

        val result = exerciseDetailsDao.getMuscleNames()
        Logs().debug("Muscle names: $result")
        val cleanedResult = result.map { it.replace("\"", "").replace("[", "").replace("]", "") }
        return cleanedResult
    }

    override suspend fun searchExercises(search: String): List<ExerciseDetails> {
        return exerciseDetailsDao.searchExercises(search)
    }

    override suspend fun searchExercisesWithFilters(
        search: String?,
        muscles: List<String>?,
        equipment: String?,
        level: String?,
        force: String?,
        mechanic: String?,
        category: String?
    ): List<ExerciseDetails> {

        return exerciseDetailsDao.searchWithFilters(
            search = search,
            muscles = muscles,
            equipment = equipment,
            level = level,
            force = force,
            mechanic = mechanic,
            category = category
        )
    }
}
