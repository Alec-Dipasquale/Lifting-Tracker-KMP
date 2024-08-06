package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.squalec.liftingtracker.utils.CustomDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutSessionScreen(
    date: CustomDate,
    navController: NavController
) {
    val viewModel: WorkoutSessionViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.handleEvent(WorkoutSessionEvent.OnChangeDate(date))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Display workout session
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = date.defaultFormat(),
        )
    }

}
