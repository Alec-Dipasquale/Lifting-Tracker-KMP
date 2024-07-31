package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WorkoutSessionScreen(
    date: String,
    viewModel: WorkoutSessionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()


}
