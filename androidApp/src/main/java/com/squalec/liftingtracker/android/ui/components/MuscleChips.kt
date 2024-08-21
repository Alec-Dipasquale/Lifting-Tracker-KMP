package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.squalec.liftingtracker.android.ui.themes.SearchExercisesTheme

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


@Preview
@Composable
fun ChipPreview() {
    SearchExercisesTheme {
        Chip(text = "Chest", onClick = {}, isSelected = true)
    }
}

@Preview
@Composable
fun MuscleChipsPreview() {
    SearchExercisesTheme {
        MuscleChips(
            muscleNames = listOf("Chest", "Back", "Legs", "Shoulders", "Arms"),
            selectedMuscles = listOf("Chest", "Back"),
            onMuscleClicked = {}
        )
    }
}