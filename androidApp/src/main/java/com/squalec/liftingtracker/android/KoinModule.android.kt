package com.squalec.liftingtracker.android

import com.squalec.liftingtracker.android.ui.screenExerciseDetails.ExerciseDetailsViewModel
import com.squalec.liftingtracker.android.ui.screenExerciseSearch.ExerciseSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun androidModule() = module {
    viewModel {
        ExerciseDetailsViewModel(get())
    }
    viewModel {
        ExerciseSearchViewModel(get())
    }
}