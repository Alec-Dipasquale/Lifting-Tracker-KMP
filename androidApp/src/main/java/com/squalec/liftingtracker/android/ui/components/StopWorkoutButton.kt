package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.squalec.liftingtracker.android.ui.navigation.Destination
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionEvent
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionViewModel
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManager
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel

@Composable
fun StopWorkoutButton(
    exercises: List<ExerciseSessionModel>,
    onIntent: (WorkoutSessionEvent) -> Unit,
    navController: NavController,
) {

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
                    onIntent(WorkoutSessionEvent.OnFinishedWorkout)
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