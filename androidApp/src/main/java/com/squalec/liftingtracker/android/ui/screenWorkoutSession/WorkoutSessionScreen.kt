package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManager
import com.squalec.liftingtracker.appdatabase.models.UserSet
import com.squalec.liftingtracker.appdatabase.repositories.SetSessionModel
import com.squalec.liftingtracker.utils.CustomDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutSessionScreen(
    date: CustomDate? = null,
    navController: NavController,
    addedExerciseId: String? = null
) {
    val viewModel: WorkoutSessionViewModel = koinViewModel()
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
                    text = state.date?.displayFormat() ?: "Unknown",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        state.workoutSessionModel?.exercises?.let { exercises ->
            item {
                val isExercisesEmpty = exercises.isNullOrEmpty()
                val buttonText = if (!isExercisesEmpty) "Finish Workout" else "Cancel Workout"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (!isExercisesEmpty)
                                viewModel.handleEvent(WorkoutSessionEvent.OnFinishedWorkout)
                            else {
                                WorkoutSessionManager.stopWorkout()
                                navController.navigate(Destination.Home) {
                                    popUpTo(Destination.Home) {
                                        inclusive = true
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = buttonText,
                            style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                }
            }

            items(exercises) { exercise ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {

                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = exercise.exercise?.name ?: "Unknown",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Column {
                            exercise.sets.let { sets ->
                                sets.forEachIndexed { index, it ->

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        TextField(
//                                            modifier = Modifier.weight(1f),
                                            value = it.weight?.toString() ?: "",
                                            onValueChange = { value ->
                                                viewModel.handleEvent(
                                                    WorkoutSessionEvent.OnUpdateWorkout(
                                                        state.workoutSessionModel!!.copy(
                                                            exercises = state.workoutSessionModel!!.exercises.map { exerciseModel ->
                                                                if (exerciseModel.orderPosition == exercise.orderPosition) {
                                                                    exerciseModel.copy(
                                                                        sets = exerciseModel.sets.map { setModel ->
                                                                            if (setModel.orderPosition == index) {
                                                                                setModel.copy(weight = value.toFloat())
                                                                            } else {
                                                                                setModel
                                                                            }
                                                                        }
                                                                    )
                                                                } else {
                                                                    exerciseModel
                                                                }
                                                            }

                                                        )
                                                    )
                                                )
                                            },
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        TextField(
                                            value = it.reps?.toString() ?: "",
                                            onValueChange = { value ->
                                                viewModel.handleEvent(
                                                    WorkoutSessionEvent.OnUpdateWorkout(
                                                        state.workoutSessionModel!!.copy(
                                                            exercises = state.workoutSessionModel!!.exercises.map { exerciseModel ->
                                                                if (exerciseModel.orderPosition == exercise.orderPosition) {
                                                                    exerciseModel.copy(
                                                                        sets = exerciseModel.sets.map { setModel ->
                                                                            if (setModel.orderPosition == index) {
                                                                                setModel.copy(reps = value.toInt())
                                                                            } else {
                                                                                setModel
                                                                            }
                                                                        }
                                                                    )
                                                                } else {
                                                                    exerciseModel
                                                                }
                                                            }
                                                        )
                                                    )
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                            Button(onClick = {
                                viewModel.handleEvent(
                                    WorkoutSessionEvent.OnAddSet(
                                        SetSessionModel(
                                            orderPosition = exercise.sets.size
                                        )
                                    )
                                )
                            }) {
                                Text(text = "Add Set")
                            }
                        }

                    }
                }

            }
        }

        if (workoutManagerState.isWorkoutInProgress)
            item {
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
    }

    BackHandler {
        navController.navigate(Destination.Home) {
            popUpTo(Destination.Home) {
                inclusive = true
            }
        }
    }

}

