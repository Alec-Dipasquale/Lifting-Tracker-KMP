package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.squalec.liftingtracker.android.ui.navigation.Destination
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionEvent
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.android.ui.utilities.LedgerIcons
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManager
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel

@Composable
fun StopWorkoutButton(
    modifier: Modifier,
    exercises: List<ExerciseSessionModel>,
    onIntent: (WorkoutSessionEvent) -> Unit,
    navController: NavController,
    isHidden: Boolean = false,
) {
    if (isHidden) return

    val isExercisesEmpty = exercises.isNullOrEmpty()
    val buttonText = if (!isExercisesEmpty) "Finish Workout" else "Cancel Workout"
    Box(
        modifier = modifier
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

@Composable
fun StopWorkoutButtonV2(
    modifier: Modifier,
    onClick: () -> Unit,
    icon: ImageVector, // Pass your custom icon here
    buttonText: String = "FINISH WORKOUT",
    backgroundColor: Color = Color(0xFF4DB6E1), // Replace with your theme's color
    textColor: Color = Color.White
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Main button background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {


            // Button text
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    text = buttonText.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }

            // Icon with a circular background
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .border(
                        color = backgroundColor,
                        width = 6.dp,
                        shape = CircleShape
                    )
                    .size(56.dp)
                    .clip(CircleShape), // Circle's background
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF151531))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = backgroundColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewStopWorkoutButton() {
    WorkoutSessionTheme {
        StopWorkoutButtonV2(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* Handle click here */ },
            icon = LedgerIcons.Custom.LiftingLedgerIcon(),
            buttonText = "Finish Workout",
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}