package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.squalec.liftingtracker.android.ui.screenCalendar.CalendarIntent
import com.squalec.liftingtracker.android.ui.screenCalendar.CalendarWorkoutDialogueState
import com.squalec.liftingtracker.android.ui.themes.CalendarViewTheme
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.utils.CustomDate

@Composable
fun CalendarWorkoutsDialogue(
    state: CalendarWorkoutDialogueState,
    onIntent: (CalendarIntent) -> Unit = { },
    navController: NavController
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
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)

            ) {
                Text(
                    text = "Workouts for ${day.formattedToDay()}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(workoutSessions) { workoutSession ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface,
                                    MaterialTheme.shapes.small
                                )
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable {
                                    onIntent(
                                        CalendarIntent.OnWorkoutSelected(
                                            workoutSession,
                                            navController = navController
                                        )
                                    )
                                }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.End
                        ) {
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
                                Text(
                                    text = "Exercises",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(end = 16.dp)
                                )
                            }
                            Column(
                                Modifier
                                    .padding(4.dp)
                                    .padding(end = 8.dp)
                            ) {
                                workoutSession.exercises.forEach {
                                    Text(
                                        text = "${it.exercise?.name ?: "Unknown"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewCalendarWorkoutsDialogue() {
    CalendarViewTheme {
        CalendarWorkoutsDialogue(
            navController = rememberNavController(),
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
}