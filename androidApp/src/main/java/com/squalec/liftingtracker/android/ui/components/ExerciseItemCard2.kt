package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.android.ui.utilities.LedgerIcons

@Composable
fun MachineTypeSection(
    modifier: Modifier = Modifier,
    onSetChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onRepsChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            Icon(
                imageVector = LedgerIcons.Custom.LiftingLedgerIcon(),
                contentDescription = "label Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "MACHINE TYPE",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                InputRow(
                    modifier = Modifier.fillMaxWidth(0.5f).padding(start = 8.dp),
                    label = "WEIGHT",
                    icon = LedgerIcons.Custom.LiftingLedgerIcon(), // Replace with your custom icon
                    placeholder = "Enter Weight",
                    onValueChange = onWeightChange
                )
                Spacer(modifier = Modifier.height(8.dp))
                InputRow(
                    modifier = Modifier.padding(start = 8.dp),
                    label = "REPS",
                    icon = LedgerIcons.Custom.LiftingLedgerIcon(), // Replace with your custom icon
                    placeholder = "Enter Reps",
                    onValueChange = onRepsChange
                )
            }
        }
    }
}

@Composable
fun InputRow(
    label: String,
    icon: ImageVector,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = "",
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder) },
            modifier = Modifier.weight(2f)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MachineTypeSectionPreview() {
    WorkoutSessionTheme {
        MachineTypeSection(
            onSetChange = {},
            onWeightChange = {},
            onRepsChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputRowPreview() {
    WorkoutSessionTheme {
        InputRow(
            label = "SET",
            icon = LedgerIcons.Custom.LiftingLedgerIcon(),
            placeholder = "Enter Set",
            onValueChange = {}
        )
    }
}
