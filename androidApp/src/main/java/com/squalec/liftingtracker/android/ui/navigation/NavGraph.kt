package com.squalec.liftingtracker.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.squalec.liftingtracker.android.ui.LoadingScreen
import com.squalec.liftingtracker.android.ui.screenExerciseDetails.ExerciseDetailScreen
import com.squalec.liftingtracker.android.ui.screenExerciseSearch.ExerciseSearchScreen
import com.squalec.liftingtracker.android.ui.screenHome.HomeScreen
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionScreen
import com.squalec.liftingtracker.android.ui.themes.ExerciseDetailTheme
import com.squalec.liftingtracker.android.ui.themes.HomeTheme
import com.squalec.liftingtracker.android.ui.themes.SearchExercisesTheme
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.utils.CustomDate
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun NavGraph(isLoading: Boolean) {

    val navController = rememberNavController()
    if (isLoading) {
        LoadingScreen()
        return
    }

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
        composable<Destination.ExerciseSearch> {
            SearchExercisesTheme {
                ExerciseSearchScreen(navController = navController)
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
            val customDate: CustomDate
            if(dateParam == null) {

                val currentDateTime = dateParam?: LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formattedDate = currentDateTime.format(formatter)
                customDate = CustomDate(formattedDate)
            } else {
                customDate = CustomDate(dateParam)
            }

            WorkoutSessionTheme {
                WorkoutSessionScreen(navController = navController, date = customDate)
            }
        }
    }
}

