package com.squalec.liftingtracker.di

import ExerciseDetailDao
import com.squalec.liftingtracker.appdatabase.AppDatabase
import com.squalec.liftingtracker.appdatabase.DBFactory
import com.squalec.liftingtracker.appdatabase.dao.UserExerciseDao
import com.squalec.liftingtracker.appdatabase.dao.UserSetDao
import com.squalec.liftingtracker.appdatabase.dao.UserWorkoutSessionDao
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseDetailsRepository
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseDetailsRepositoryImpl
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionRepository
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionRepositoryImpl
import org.koin.dsl.module

val databaseModule = module {

        single{ DBFactory.createDatabase() }

        single<ExerciseDetailDao>{ get<AppDatabase>().exerciseDao() }

        single<UserWorkoutSessionDao>{ get<AppDatabase>().userWorkoutSessionDao() }
        single<UserExerciseDao>{ get<AppDatabase>().userExerciseDao() }
        single<UserSetDao>{ get<AppDatabase>().userSetDao() }

        single<ExerciseDetailsRepository> { ExerciseDetailsRepositoryImpl(get()) }
        single<WorkoutSessionRepository> { WorkoutSessionRepositoryImpl(get(), get(), get(), get()) }

}