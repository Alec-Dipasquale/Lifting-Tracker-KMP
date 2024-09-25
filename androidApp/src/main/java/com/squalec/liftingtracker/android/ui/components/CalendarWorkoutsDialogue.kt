package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.squalec.liftingtracker.android.ui.screenCalendar.CalendarIntent
import com.squalec.liftingtracker.android.ui.screenCalendar.CalendarWorkoutDialogueState
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.utils.CustomDate

@Composable
fun CalendarWorkoutsDialogue(
    state: CalendarWorkoutDialogueState,
    onIntent: (CalendarIntent) -> Unit = { }
) {

    val workoutSessions = state.workoutSessions
    val day = state.day

    Dialog(onDismissRequest = { onIntent(CalendarIntent.CloseWorkoutsDialogue) }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
                    .padding(16.dp)

            ) {
                Text(
                    text = "Workouts for ${day.formattedToDay()}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn {
                    items(workoutSessions) { workoutSession ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = workoutSession.workoutName,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Column {
                                workoutSession.exercises.forEach {
                                    Text(
                                        text = "${it.exercise?.name ?: "Unknown"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewCalendarWorkoutsDialogue() {
    CalendarWorkoutsDialogue(
        state = CalendarWorkoutDialogueState(
            day = CustomDate("2022-01-01T00:00:00"),
            workoutSessions = listOf(
                WorkoutSessionModel(
                    workoutName = "Workout 1",
                    exercises = listOf(
                        ExerciseSessionModel(
                            ExerciseDetails(
                                id = "",
                                category = "Chest",
                                name = "Bench Press",
                                level = "3"
                            ),
                            orderPosition = 1,
                            sets = listOf(),
                        )
                    ),
                    date = CustomDate.now()
                ),
                WorkoutSessionModel(
                    workoutName = "Workout 2",
                    exercises = listOf(
                        ExerciseSessionModel(
                            ExerciseDetails(
                                id = "",
                                category = "Chest",
                                name = "Chest Flies",
                                level = "3"
                            ),
                            orderPosition = 1,
                            sets = listOf(),
                        )
                    ),
                    date = CustomDate.now()
                )
            ),
            isWorkoutsDialogueOpen = true
        )
    )
}