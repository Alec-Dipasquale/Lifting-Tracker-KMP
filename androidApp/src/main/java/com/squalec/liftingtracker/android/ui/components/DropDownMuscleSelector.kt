package com.squalec.liftingtracker.android.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squalec.liftingtracker.android.ui.screenExerciseSearch.ExerciseSearchIntent
import com.squalec.liftingtracker.android.ui.screenExerciseSearch.MuscleFilterState
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionEvent
import com.squalec.liftingtracker.android.ui.themes.SearchExercisesTheme
import com.squalec.liftingtracker.appdatabase.repositories.Operator

@Composable
fun DropDownMuscleSelector(
    modifier: Modifier = Modifier,
    muscleNames: List<String>,
    selectedMuscles: List<String>,
    onMuscleClicked: (List<String>) -> Unit,
    onIntent: (ExerciseSearchIntent) -> Unit,
    filterState: MuscleFilterState,
    isExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                .padding(vertical = 16.dp)
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand muscles")
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
            Column {
                MuscleChipFilters(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    onIntent = { onIntent(it) },
                    state = filterState
                )
                MuscleChips(
                    muscleNames = muscleNames,
                    selectedMuscles = selectedMuscles,
                    onMuscleClicked = {
                        onMuscleClicked(it)
                    })
            }
        }


    }
}

@Composable
fun MuscleChipFilters(
    modifier: Modifier = Modifier,
    onIntent: (ExerciseSearchIntent) -> Unit,
    state: MuscleFilterState,
) {


    Column(modifier = modifier) {
        Row {
            Chip(
                text = "And",
                onClick = {
                    onIntent(ExerciseSearchIntent.OperatorChanged(Operator.AND))
                          },
                isSelected = if (state.operatorSelected == Operator.AND) true else false
            )
            Spacer(modifier = Modifier.width(8.dp))
            Chip(
                text = "Or",
                onClick = {
                    onIntent(ExerciseSearchIntent.OperatorChanged(Operator.OR))
                },
                isSelected = if (state.operatorSelected == Operator.OR) true else false
            )
        }
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
    SearchExercisesTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DropDownMuscleSelector(
                isExpanded = true,
                muscleNames = muscles,
                selectedMuscles = selectedMuscles,
                onMuscleClicked = { newSelectedMuscles ->
                    selectedMuscles = newSelectedMuscles
                },
                onIntent = {},
                filterState = MuscleFilterState()
            )
        }
    }
}