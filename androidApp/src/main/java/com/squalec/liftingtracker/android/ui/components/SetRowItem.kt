package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionEvent
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.SetSessionModel

@Composable
fun SetRowItem(
    modifier: Modifier = Modifier,
    set: SetSessionModel,
    exercise: ExerciseSessionModel,
    onIntent: (WorkoutSessionEvent) -> Unit
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${set.orderPosition + 1}")
            TextField(
                modifier = Modifier
                    .width(90.dp)
                    .clip(shape = RoundedCornerShape(4.dp)),
                value = set.weight?.toString() ?: "",
                onValueChange = { value ->
                    onIntent(WorkoutSessionEvent.ChangeSetWeight(exercise, set, value.toFloat()))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Text(text = "x")
            TextField(
                modifier = Modifier
                    .width(90.dp)
                    .clip(shape = RoundedCornerShape(4.dp)),
                value = set.reps?.toString() ?: "",
                onValueChange = { value ->
                    onIntent(WorkoutSessionEvent.ChangeSetReps(exercise, set, value.toInt()))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}

@Preview
@Composable
fun SetRowItemPreview() {
    WorkoutSessionTheme(darkTheme = false) {
        SetRowItem(
            set = SetSessionModel(),
            exercise = ExerciseSessionModel(exercise = null, sets = emptyList(), orderPosition = 0),
            onIntent = {}
        )
    }
}