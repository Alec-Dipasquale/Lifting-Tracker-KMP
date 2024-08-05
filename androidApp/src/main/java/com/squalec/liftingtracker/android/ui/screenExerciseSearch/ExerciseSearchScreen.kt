package com.squalec.liftingtracker.android.ui.screenExerciseSearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.wear.compose.material.ContentAlpha
import com.google.accompanist.flowlayout.FlowRow
import com.squalec.liftingtracker.android.ui.navigation.Destination
import org.koin.androidx.compose.koinViewModel


@Composable
fun ExerciseSearchScreen(
    navController: NavController
) {
    val viewModel: ExerciseSearchViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
    }

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
            OutlinedTextField(
                value = state.searchText,
                onValueChange = {
                    viewModel.intent(ExerciseSearchIntent.SearchExercises(it))
                },
                label = { Text("Search Exercises") },
                placeholder = { Text("Enter exercise name") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
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
                }
            )
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
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(16.dp)
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


@Composable
fun DropDownMuscleSelector(
    modifier: Modifier = Modifier,
    muscleNames: List<String>,
    selectedMuscles: List<String>,
    onMuscleClicked: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row( modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded }
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon( Icons.Default.ArrowDropDown, contentDescription = "Expand muscles")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (selectedMuscles.isEmpty()) "Select muscles" else selectedMuscles.joinToString(),
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), visible = expanded
        ) {
            MuscleChips(
                muscleNames = muscleNames,
                selectedMuscles = selectedMuscles,
                onMuscleClicked = {
                    onMuscleClicked(it)
                })
        }


    }
}


@Composable
fun MuscleChips(
    muscleNames: List<String>,
    selectedMuscles: List<String>,
    onMuscleClicked: (List<String>) -> Unit
) {
    FlowRow(
        modifier = Modifier.padding(8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        muscleNames.sorted().forEach { muscle ->
            Chip(
                text = muscle,
                isSelected = selectedMuscles.contains(muscle),
                onClick = {
                    val newSelectedMuscles = if (selectedMuscles.contains(muscle)) {
                        selectedMuscles - muscle
                    } else {
                        selectedMuscles + muscle
                    }
                    onMuscleClicked(newSelectedMuscles)
                }
            )
        }
    }
}


@Composable
fun Chip(text: String, onClick: () -> Unit, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .wrapContentWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDropDownMuscleSelector() {
    val muscles = listOf(
        "Biceps",
        "Triceps",
        "Quadriceps",
        "Hamstrings",
        "Deltoids",
        "Pectorals",
        "Abdominals",
        "Gluteals",
        "Lats"
    )
    var selectedMuscles by remember { mutableStateOf(listOf<String>()) }

    DropDownMuscleSelector(
        muscleNames = muscles,
        selectedMuscles = selectedMuscles,
        onMuscleClicked = { newSelectedMuscles ->
            selectedMuscles = newSelectedMuscles
        }
    )
}
