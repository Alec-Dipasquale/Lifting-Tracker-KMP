package com.squalec.liftingtracker.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.squalec.liftingtracker.android.ui.LoadingScreen
import com.squalec.liftingtracker.android.ui.ScreenExerciseSearch.ExerciseSearchScreen
import com.squalec.liftingtracker.android.ui.ScreenHome.HomeScreen

@Composable
fun NavGraph(isLoading: Boolean) {

    val navController = rememberNavController()
    if(isLoading) {
        LoadingScreen()
        return
    }

    NavHost(navController = navController, startDestination = ExerciseSearchDestination) {
        composable<LoadingDestination> {
             LoadingScreen()
        }
        composable<HomeDestination> {
            HomeScreen()
        }
        composable<ExerciseSearchDestination> {
            ExerciseSearchScreen()
        }
    }
}