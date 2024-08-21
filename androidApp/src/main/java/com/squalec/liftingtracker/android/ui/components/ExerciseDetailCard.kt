package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.squalec.liftingtracker.android.ui.themes.SearchExercisesTheme
import com.squalec.liftingtracker.android.ui.utilities.ShadowTypes
import com.squalec.liftingtracker.android.ui.utilities.customShadow

@Composable
fun ExerciseDetailCard(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .customShadow(ShadowTypes.medium)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
fun ExerciseDetailCardPreview() {
    SearchExercisesTheme {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            ExerciseDetailCard(
                title = "Exercise",
                content = "Bench Press"
            )
        }
    }
}