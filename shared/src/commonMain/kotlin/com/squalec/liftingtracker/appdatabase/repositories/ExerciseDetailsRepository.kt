package com.squalec.liftingtracker.appdatabase.repositories

import ExerciseDetailDao
import com.squalec.liftingtracker.appdatabase.Logs
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails

interface ExerciseDetailsRepository{
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

    suspend  fun getExerciseDetails(id: String): ExerciseDetails?

}
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

    override suspend fun getExerciseDetails(id: String): ExerciseDetails? {
        return exerciseDetailsDao.getExerciseDetailsById(id)
    }
}
