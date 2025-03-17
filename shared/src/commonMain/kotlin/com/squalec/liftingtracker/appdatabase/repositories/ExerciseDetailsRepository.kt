package com.squalec.liftingtracker.appdatabase.repositories

import ExerciseDetailDao
import com.squalec.liftingtracker.appdatabase.Logs
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails

interface ExerciseDetailsRepository {
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
        category: String? = null,
        operatorFilter: Operator
    ): List<ExerciseDetails>

    suspend fun getExerciseDetails(id: String): ExerciseDetails?

}

class ExerciseDetailsRepositoryImpl(
    private val exerciseDetailsDao: ExerciseDetailDao
) : ExerciseDetailsRepository {

    override suspend fun getAllExerciseDetails(): List<ExerciseDetails> {
       try {
           return exerciseDetailsDao.getAllExercises()
       } catch (e: Exception) {
           Logs().error("Error getting all exercises from local db: ${e.message}")
           return emptyList()
       }
    }

    override suspend fun insertAllExerciseDetails(exerciseDetails: List<ExerciseDetails>) {
        try {
            exerciseDetailsDao.insertAll(exerciseDetails)
        } catch (e: Exception) {
            Logs().error("Error inserting all exercises to local db: ${e.message}")
        }
    }

    override suspend fun muscleNames(): List<String> {

        try {
            val result = exerciseDetailsDao.getMuscleNames()
            Logs().debug("Grabbed muscle names from local db with ${result.size} results")
            val cleanedResult = result.map { it.replace("\"", "").replace("[", "").replace("]", "") }
            return cleanedResult
        } catch (e: Exception) {
            Logs().error("Error getting muscle names from local db: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun searchExercises(search: String): List<ExerciseDetails> {
        try {
            val results = exerciseDetailsDao.searchExercises(search)
            Logs().debug("Search results Successful returning ${results.size} results")
            return results
        } catch (e: Exception) {
            Logs().error("Error searching exercises from local db: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun searchExercisesWithFilters(
        search: String?,
        muscles: List<String>?,
        equipment: String?,
        level: String?,
        force: String?,
        mechanic: String?,
        category: String?,
        operatorFilter: Operator
    ): List<ExerciseDetails> {

        try {
            val results: List<ExerciseDetails>
            val muscle1 = muscles?.getOrNull(0)
            val muscle2 = muscles?.getOrNull(1)
            val muscle3 = muscles?.getOrNull(2)
            val muscle4 = muscles?.getOrNull(3)
            val muscle5 = muscles?.getOrNull(4)
            val muscle6 = muscles?.getOrNull(5)
            val muscle7 = muscles?.getOrNull(6)
            if (operatorFilter == Operator.OR) {
                // Call the DAO method with the extracted muscles and other filters
                results = exerciseDetailsDao.searchWithAnyMuscle(
                    search = search,
                    muscle1 = muscle1,
                    muscle2 = muscle2,
                    muscle3 = muscle3,
                    muscle4 = muscle4,
                    muscle5 = muscle5,
                    muscle6 = muscle6,
                    muscle7 = muscle7,
//                equipment = equipment,
//                level = level,
//                force = force,
//                mechanic = mechanic,
//                category = category
                )
            } else {
                // Implement behavior for other operators if necessary
                // For example, handling AND operator logic

                results = exerciseDetailsDao.searchWithAllMuscles(
                    search = search,
                    muscle1 = muscle1,
                    muscle2 = muscle2,
                    muscle3 = muscle3,
                    muscle4 = muscle4,
                    muscle5 = muscle5,
                    muscle6 = muscle6,
                    muscle7 = muscle7,
                    equipment = equipment,
                    level = level,
                    force = force,
                    mechanic = mechanic,
                    category = category

                )
            }

            Logs().debug("Search results Successful with filters returning ${results.size} results")

            return results

        } catch (e: Exception) {
            Logs().error("Error searching exercises with filters from local db: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun getExerciseDetails(id: String): ExerciseDetails? {
        try {
            return exerciseDetailsDao.getExerciseDetailsById(id)
        } catch (e: Exception) {
            Logs().error("Error getting exercise details by id from local db: ${e.message}")
            return null
        }
    }
}

enum class Operator {
    AND,
    OR
}