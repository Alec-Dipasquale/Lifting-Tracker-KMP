package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.squalec.liftingtracker.android.ui.navigation.Destination
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

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Display workout session
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = date.displayFormat(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        state.workoutSessionModel?.exercises?.let { exercises ->
            items(exercises) { exercise ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = exercise.exercise?.name ?: "Unknown",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
            }

        }

        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(Destination.ExerciseSearch)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Add Exercise",
                        style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }
        }
    }

}
