package com.squalec.liftingtracker.android.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.squalec.liftingtracker.android.ui.LoadingScreen
import com.squalec.liftingtracker.android.ui.screenCalendar.CalendarView
import com.squalec.liftingtracker.android.ui.screenExerciseDetails.ExerciseDetailScreen
import com.squalec.liftingtracker.android.ui.screenExerciseSearch.ExerciseSearchScreen
import com.squalec.liftingtracker.android.ui.screenHome.HomeScreen
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionScreen
import com.squalec.liftingtracker.android.ui.themes.CalendarViewTheme
import com.squalec.liftingtracker.android.ui.themes.ExerciseDetailTheme
import com.squalec.liftingtracker.android.ui.themes.HomeTheme
import com.squalec.liftingtracker.android.ui.themes.SearchExercisesTheme
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManager
import com.squalec.liftingtracker.utils.CustomDate
import kotlinx.coroutines.delay

@Composable
fun NavGraph(isLoading: Boolean) {

    val navController = rememberNavController()
    var isInWorkoutSession by remember { mutableStateOf(false) }

    if (isLoading) {
        LoadingScreen()
        return
    }
    WorkoutInProgressBar(
        navController = navController,
        isInWorkoutSession = isInWorkoutSession
    ) {
        NavHost(
            navController = navController,
            startDestination = Destination.Home
        ) {
            composable<Destination.Loading> {
                LoadingScreen()
            }
            composable<Destination.Home> {

                HomeTheme {
                    HomeScreen(navController = navController)
                }
            }
            composable<Destination.ExerciseSearch> { backStackEntry ->
                val isOnClickExerciseEnabled =
                    backStackEntry.arguments?.getBoolean("isOnClickExerciseEnabled") ?: false
                SearchExercisesTheme {
                    ExerciseSearchScreen(
                        navController = navController,
                        isOnClickExerciseEnabled = isOnClickExerciseEnabled
                    )
                }
            }
            composable<Destination.ExerciseDetail> { backStackEntry ->
                ExerciseDetailTheme {
                    val exerciseId = backStackEntry.toRoute<Destination.ExerciseDetail>()
                    ExerciseDetailScreen(
                        navController = navController,
                        exerciseId = exerciseId.exerciseId
                    )
                }
            }
            composable<Destination.WorkoutSession> { parameters ->
                val dateParam = parameters.arguments?.getString("date")
                val addedExerciseId = parameters.arguments?.getString("addedExerciseId")

                val customDate = dateParam?.let {
                    CustomDate(dateParam)
                }

                isInWorkoutSession = true

                WorkoutSessionTheme {
                    WorkoutSessionScreen(
                        navController = navController,
                        date = customDate,
                        addedExerciseId = addedExerciseId
                    )
                }
            }

            composable<Destination.CalendarView> {
                CalendarViewTheme {
                    CalendarView()
                }
            }
        }
    }
}

@Composable
fun WorkoutInProgressBar(
    isInWorkoutSession: Boolean,
    navController: NavController,
    content: @Composable () -> Unit) {
    val isWorkoutInProgress by WorkoutSessionManager.workoutState.collectAsState()
    if (!isWorkoutInProgress.isWorkoutInProgress) {
        content()
        return
    }

    var workoutDuration by remember { mutableLongStateOf(WorkoutSessionManager.getWorkoutDuration()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L) // Update every second
            workoutDuration = WorkoutSessionManager.getWorkoutDuration()
        }
    }

    Scaffold(
        bottomBar = {
            if (isInWorkoutSession) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
                    .clickable {
                        navController.navigate(Destination.WorkoutSession())
                    }
                Row(
                    modifier = modifier.height(60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = formatTime(workoutDuration),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                content()
            }
        }
    )
}

private fun formatTime(time: Long): String {
    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = time % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
