package com.squalec.liftingtracker.android.ui.screenExerciseSearch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ContentAlpha
import com.squalec.liftingtracker.android.ui.components.BackgroundDefault
import com.squalec.liftingtracker.android.ui.components.DropDownMuscleSelector
import com.squalec.liftingtracker.android.ui.navigation.Destination
import com.squalec.liftingtracker.android.ui.utilities.ShadowTypes
import com.squalec.liftingtracker.android.ui.utilities.customShadow
import org.koin.androidx.compose.koinViewModel


@Composable
fun ExerciseSearchScreen(
    navController: NavController,
    isOnClickExerciseEnabled: Boolean
) {
    val viewModel: ExerciseSearchViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    BackgroundDefault {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header section with TextField and DropDownMuscleSelector
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 16.dp)
                    .zIndex(1f) // Ensure it stays on top of the list when scrolling
            ) {
                TextField(
                    value = state.searchText,
                    onValueChange = {
                        viewModel.intent(ExerciseSearchIntent.SearchExercises(it))
                    },
                    label = { Text("Search Exercises") },
                    placeholder = { Text("Enter exercise name") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                DropDownMuscleSelector(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally),
                    muscleNames = state.muscleNames ?: emptyList(),
                    selectedMuscles = state.filters.muscle ?: emptyList(),
                    onMuscleClicked = { musclesSelected ->
                        viewModel.intent(
                            ExerciseSearchIntent.UpdateFilter(
                                ExerciseFilters(
                                    muscle = musclesSelected
                                )
                            )
                        )
                    },
                    onIntent = { viewModel.intent(it) },
                    filterState = state.muscleFilterState
                )
                Icons
            }

            // Scrollable exercise list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .padding(horizontal = 24.dp)
            ) {
                items(state.exercises) { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .customShadow(ShadowTypes.medium)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .clickable {
                                if (isOnClickExerciseEnabled) {
                                    navController.navigate(
                                        Destination.WorkoutSession(
                                            addedExerciseId = exercise.id
                                        )
                                    ) {
                                        popUpTo(Destination.WorkoutSession()) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                            .padding(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(0.6f),
                            text = exercise.name
                        )
                        Icon(
                            modifier = Modifier.clickable {
                                navController.navigate(Destination.ExerciseDetail(exercise.id))
                            },
                            imageVector = Icons.Default.Info,
                            contentDescription = "Exercise Details"
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun ExerciseSearchScreenPreview() {
    ExerciseSearchScreen(navController = rememberNavController(), isOnClickExerciseEnabled = true)
}