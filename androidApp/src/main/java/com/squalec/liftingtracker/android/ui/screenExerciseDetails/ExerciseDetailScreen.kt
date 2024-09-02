package com.squalec.liftingtracker.android.ui.screenExerciseDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import com.squalec.liftingtracker.android.ui.components.ExerciseDetailCard
import com.squalec.liftingtracker.android.ui.components.ExerciseDetailListCard
import com.squalec.liftingtracker.android.ui.components.ImageCarousel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExerciseDetailScreen(
    viewModel: ExerciseDetailsViewModel = koinViewModel(),
    exerciseId: String,
    navController: NavHostController
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.intent(ExerciseDetailsIntent.GetExercise(exerciseId))
    }

    LazyColumn(
        modifier = Modifier
            .scrollable(orientation = Orientation.Vertical, state = rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Column {
                Text(
                    text = state.exerciseDetails?.name ?: "No Name",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Divider(color = MaterialTheme.colorScheme.onBackground, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
                ImageCarousel(state.exerciseImageIds)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        item {
            ExerciseDetailListCard(
                title = "Primary Muscle",
                content = state.exerciseDetails?.primaryMuscles
                    ?: listOf("No Primary Muscle Listed")
            )
        }
        item {
            ExerciseDetailListCard(
                title = "Secondary Muscle",
                content = state.exerciseDetails?.secondaryMuscles
                    ?: listOf("No Secondary Muscle Listed")
            )
        }
        item {
            ExerciseDetailCard(
                title = "Equipment",
                content = state.exerciseDetails?.equipment ?: "No Equipment Listed"
            )
        }
        item {
            ExerciseDetailCard(
                title = "Mechanics",
                content = state.exerciseDetails?.mechanic ?: "No Mechanics Listed"
            )
        }
        item {
            ExerciseDetailCard(
                title = "Level",
                content = state.exerciseDetails?.level ?: "No Level Listed"
            )
        }
        item {
            ExerciseDetailCard(
                title = "Force",
                content = state.exerciseDetails?.force ?: "No Force Listed"
            )
        }
        item {
            ExerciseDetailCard(
                title = "Category",
                content = state.exerciseDetails?.category ?: "No Category Listed"
            )
        }
        item {
            ExerciseDetailListCard(
                title = "Instructions",
                content = state.exerciseDetails?.instructions ?: listOf("No Instructions Listed")
            )
        }
    }
}