package com.squalec.liftingtracker.android.ui.ScreenExerciseSearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.exerciseSearch.ExerciseSearchIntent
import com.squalec.liftingtracker.exerciseSearch.ExerciseSearchViewModel

@Composable
fun ExerciseSearchScreen(
    exerciseSearchViewModel: ExerciseSearchViewModel = viewModel() // Get ViewModel instance
) {


    val state by exerciseSearchViewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        TextField(value = state.searchText, onValueChange = {
            exerciseSearchViewModel.intent(ExerciseSearchIntent.SearchExercises(it))
        })
        LazyColumn {
            items(state.exercises) { exercise ->
                Row {
                    Text(modifier = Modifier.fillMaxWidth(0.6f), text = exercise.name)

                    Text(text = exercise.primaryMuscles?.first()?: "")
                }
            }
        }
    }

}