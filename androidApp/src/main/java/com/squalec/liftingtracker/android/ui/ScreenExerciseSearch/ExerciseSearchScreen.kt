package com.squalec.liftingtracker.android.ui.ScreenExerciseSearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExerciseSearchScreen(
    exerciseSearchViewModel: ExerciseSearchViewModel = viewModel() // Get ViewModel instance
) {


    val state by exerciseSearchViewModel.state.collectAsState()

    SideEffect {
        exerciseSearchViewModel.intent(ExerciseSearchIntent.GetAllMuscleNames)
    }

    if(state.isExerciseLoading) {
        // Show loading indicator
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Loading...")
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        DropDownMuscleSelector(
            modifier = Modifier.fillMaxWidth(0.8f),
            muscleNames = state.muscleNames?: emptyList(),
            selectedMuscles = state.filters.muscle?: emptyList(),
            onMuscleClicked = { musclesSelected ->
                exerciseSearchViewModel.intent(ExerciseSearchIntent.UpdateFilter(ExerciseFilters(muscle = musclesSelected)))
            }
        )
        TextField(value = state.searchText, onValueChange = {
            exerciseSearchViewModel.intent(ExerciseSearchIntent.SearchExercises(it))
        })
        LazyColumn {
            items(state.exercises) { exercise ->
                Row {
                    Text(modifier = Modifier.fillMaxWidth(0.6f), text = exercise.name)

                    Text(text = exercise.primaryMuscles?.first() ?: "")
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
        BasicTextField(
            value = if (selectedMuscles.isEmpty()) "Select muscles" else selectedMuscles.joinToString(),
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            textStyle = TextStyle(color = Color.Black)
        )

        AnimatedVisibility(visible = expanded) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(muscleNames) { muscle ->
                        DropdownMenuItem(
                            text = { Text(text = muscle) },
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
            FlowRow(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                selectedMuscles.forEach { muscle ->
                    Chip(
                        text = muscle,
                        onClick = {
                            val newSelectedMuscles = selectedMuscles - muscle
                            onMuscleClicked(newSelectedMuscles)
                        }
                    )
                }
            }
        }


    }
}

@Composable
fun Chip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(Color.Black, shape = MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0
        var xPosition = 0
        var rowMaxHeight = 0

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                if (xPosition + placeable.width > constraints.maxWidth) {
                    xPosition = 0
                    yPosition += rowMaxHeight
                    rowMaxHeight = 0
                }
                placeable.placeRelative(xPosition, yPosition)
                xPosition += placeable.width
                rowMaxHeight = maxOf(rowMaxHeight, placeable.height)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDropDownMuscleSelector() {
    val muscles = listOf("Biceps", "Triceps", "Quadriceps", "Hamstrings", "Deltoids", "Pectorals", "Abdominals", "Gluteals", "Lats")
    var selectedMuscles by remember { mutableStateOf(listOf<String>()) }

    DropDownMuscleSelector(
        muscleNames = muscles,
        selectedMuscles = selectedMuscles,
        onMuscleClicked = { newSelectedMuscles ->
            selectedMuscles = newSelectedMuscles
        }
    )
}
