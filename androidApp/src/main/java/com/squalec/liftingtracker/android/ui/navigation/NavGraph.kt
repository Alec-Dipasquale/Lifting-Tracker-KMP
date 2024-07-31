package com.squalec.liftingtracker.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.squalec.liftingtracker.android.ui.screenExerciseDetails.ExerciseDetailScreen
import com.squalec.liftingtracker.android.ui.LoadingScreen
import com.squalec.liftingtracker.android.ui.screenExerciseSearch.ExerciseSearchScreen
import com.squalec.liftingtracker.android.ui.screenHome.HomeScreen
import com.squalec.liftingtracker.android.ui.themes.ExerciseDetailTheme
import com.squalec.liftingtracker.android.ui.themes.HomeTheme
import com.squalec.liftingtracker.android.ui.themes.SearchExercisesTheme

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
                ExerciseDetailScreen(navController = navController, exerciseId = exerciseId.exerciseId)
            }
        }
        composable<Destination.WorkoutSession> { parameters ->
            val date = parameters.toRoute<Destination.WorkoutSession>().date
            WorkoutSession(navController = navController, date = date)
        }
    }
}

