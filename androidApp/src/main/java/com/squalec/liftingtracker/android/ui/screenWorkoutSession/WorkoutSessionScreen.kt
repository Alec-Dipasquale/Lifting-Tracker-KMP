package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.squalec.liftingtracker.android.ui.components.ExerciseItemCard
import com.squalec.liftingtracker.android.ui.components.StopWorkoutButton
import com.squalec.liftingtracker.android.ui.utilities.clearAllFocusOnTap
import com.squalec.liftingtracker.android.ui.navigation.Destination
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManagedState
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManager
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.SetSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.utils.CustomDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutSessionScreen(
    date: CustomDate? = null,
    navController: NavController,
    addedExerciseId: String? = null,
    viewModel: WorkoutSessionViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val workoutManagerState by WorkoutSessionManager.workoutState.collectAsState()


    LaunchedEffect(key1 = date) {
        if (date != null && !workoutManagerState.isWorkoutInProgress) {
            viewModel.handleEvent(WorkoutSessionEvent.OnChangeDate(date))
        }
    }

    LaunchedEffect(key1 = addedExerciseId) {
        if (addedExerciseId != null) {
            viewModel.handleEvent(WorkoutSessionEvent.OnAddExercise(addedExerciseId))
        }
    }

    WorkoutSessionLazyColumn(
        state = state,
        workoutManagerState = workoutManagerState,
        onIntent = {
            viewModel.handleEvent(it)
        },
        navController = navController
    )



    BackHandler {
        navController.navigate(Destination.Home) {
            popUpTo(Destination.Home) {
                inclusive = true
            }
        }
    }

}

@Composable
fun WorkoutSessionLazyColumn(
    state: WorkoutSessionState,
    workoutManagerState: WorkoutSessionManagedState,
    onIntent: (WorkoutSessionEvent) -> Unit,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
    LazyColumn(modifier = Modifier
        .clearAllFocusOnTap(focusManager)
        .fillMaxSize()) {
        // Display workout session
        item {
            DateTitle(
                date = state.date,
            )
        }
        state.workoutSessionModel?.exercises?.let { exercises ->
            item {
                StopWorkoutButton(
                    modifier = Modifier.clearAllFocusOnTap(focusManager).fillMaxWidth(),
                    exercises = exercises,
                    navController = navController,
                    onIntent = {
                        onIntent(it)
                    },
                    isHidden = state.isFinished
                )
            }

            items(exercises) { exercise ->
                ExerciseItemCard(
                    isFinished = state.isFinished,
                    exercise = exercise,
                    onIntent = {
                        onIntent(it)
                    }
                )
            }
        }

        if (workoutManagerState.isWorkoutInProgress)
            item {
                AddExerciseButton(
                    navController = navController
                )
            }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Preview
@Composable
fun WorkoutSessionScreenPreview() {
    WorkoutSessionTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            WorkoutSessionLazyColumn(
                navController = rememberNavController(),
                state = WorkoutSessionState(
                    date = CustomDate.now(),
                    workoutSessionModel = WorkoutSessionModel(
                        date = CustomDate.now(),
                        caloriesBurned = 0,
                        duration = 0,
                        exercises = listOf(
                            ExerciseSessionModel(
                                exercise = ExerciseDetails(
                                    id = "1",
                                    name = "Bench Press",
                                    equipment = "Barbell",
                                    level = "Intermediate",
                                    category = "Chest",

                                    ),
                                sets = listOf(
                                    SetSessionModel(
                                        orderPosition = 0
                                    )
                                ),
                                orderPosition = 0
                            )

                        )
                    )
                ),
                workoutManagerState = WorkoutSessionManagedState(
                    workoutSessionModel = WorkoutSessionModel(
                        date = CustomDate.now(),
                        caloriesBurned = 0,
                        duration = 0,
                        exercises = emptyList()
                    ),
                    isWorkoutInProgress = true,
                ),
                onIntent = {}
            )
        }
    }
}


@Composable
fun AddExerciseButton(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(Destination.ExerciseSearch(true))
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

@Composable
fun DateTitle(date: CustomDate?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = date?.displayFormat() ?: "Unknown",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

}

