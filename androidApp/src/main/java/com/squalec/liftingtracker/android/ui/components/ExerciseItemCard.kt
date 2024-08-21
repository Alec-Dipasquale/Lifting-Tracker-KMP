package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionEvent
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.android.ui.utilities.customShadow
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.SetSessionModel

@Composable
fun ExerciseItemCard(
    exercise: ExerciseSessionModel,
    onIntent: (WorkoutSessionEvent) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .customShadow(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
        ) {

            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                text = exercise.exercise?.name ?: "Unknown",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Divider(
                color = MaterialTheme.colorScheme.onSurface
            )
            Column {
                WeightSetsHeader(
                    modifier = Modifier.padding(16.dp),
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                exercise.sets.let { sets ->
                    sets.forEachIndexed { index, it ->
                        SetRowItem(
                            modifier = Modifier.padding(vertical = 8.dp),
                            set = it,
                            exercise = exercise,
                            onIntent = {
                                onIntent(it)
                            }
                        )
                        Divider(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                        onClick = {
                        onIntent(
                            WorkoutSessionEvent.OnAddSet(
                                SetSessionModel(
                                    orderPosition = exercise.sets.size
                                )
                            )
                        )
                    }) {
                        Text(text = "Add Set")
                    }
                }

            }
        }

    }
}

@Composable
fun WeightSetsHeader(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Set",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                modifier = Modifier.width(90.dp),
                text = "Weight",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = "x",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.width(90.dp),
                text = "Reps",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center

            )
        }
    }
}


@Preview
@Composable
fun ExerciseItemCardPreview() {
    WorkoutSessionTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
            ExerciseItemCard(
                exercise = ExerciseSessionModel(
                    exercise = null,
                    sets = listOf(
                        SetSessionModel(
                            orderPosition = 0
                        )
                    ),
                    orderPosition = 0
                ),
                onIntent = {}
            )
        }
    }
}



