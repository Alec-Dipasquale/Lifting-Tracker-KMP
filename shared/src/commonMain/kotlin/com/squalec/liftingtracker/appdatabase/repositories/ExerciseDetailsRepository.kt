package com.squalec.workoutmodule.data.repositories

import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails

internal interface ExerciseDetailsRepository {
    fun getAllExerciseDetails(): List<ExerciseDetails>

    fun insertAllExerciseDetails(exerciseDetails: List<ExerciseDetails>)


}


//internal class ExerciseDetailsRepositoryImpl(database: AppDatabase): ExerciseDetailsRepository {
//    val exerciseDetailsDao = database.exerciseDetailsDao()
//
//
//    override fun getAllExerciseDetails(): List<ExerciseDetails> {
//        return exerciseDetailsDao.getAllExercises()
//    }
//
//    override fun insertAllExerciseDetails(exerciseDetails: List<ExerciseDetails>) {
//        exerciseDetailsDao.insertAll(exerciseDetails)
//    }
//}

