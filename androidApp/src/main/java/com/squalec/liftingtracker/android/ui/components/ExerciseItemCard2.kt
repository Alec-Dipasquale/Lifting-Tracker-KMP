package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionEvent
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.android.ui.utilities.LedgerIcons
import com.squalec.liftingtracker.android.ui.utilities.clearAllFocusOnTap
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.SetSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WeightMetricTypes

@Composable
fun ExerciseItemCard2(
    modifier: Modifier = Modifier,
    exercise: ExerciseSessionModel,
    onWeightChange: (weight: Float, position: Int) -> Unit,
    onRepsChange: (reps: Int, position: Int) -> Unit,
    onSetAdded: (setSessionModel: SetSessionModel) -> Unit,
    onSetRemoved: () -> Unit = {},
    weightMetric: WeightMetricTypes = WeightMetricTypes.LB,
    isFinished: Boolean = false
) {
    /*
    *   todo Add exercise deletion on long press
    *   todo Add Set deletion on long press
    *   */
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)) // Clip ensures content respects rounded corners
            .background(MaterialTheme.colorScheme.surface) // Rounded corners for the card
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
    ) {
        // Top header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(8.dp)
        ) {
            Icon(
                imageVector = LedgerIcons.Custom.LiftingLedgerIcon(),
                contentDescription = "label Icon",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = exercise.exercise?.name ?: "Exercise",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        // Exercise sets
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface) // Matches card surface color
        ) {
            exercise.sets.forEachIndexed { index, set ->
                InputSetRow(
                    set = set,
                    onWeightChange = { weightChange ->
                        onWeightChange(weightChange.toFloat(), index)
                    },
                    onRepsChange = { reps ->
                        onRepsChange(reps, index)
                    },
                    weightMetric = weightMetric,
                    isFinished = isFinished,
                    cyclicColorManager = CyclicColorManager(Color(0xFFCFD3E0), 4),
                    position = index
                )
            }

            if (!isFinished) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    onClick = {
                        onSetAdded(
                            SetSessionModel(
                                orderPosition = exercise.sets.size,
                            )
                        )
                    }
                ) {
                    Text(text = "Add Set")
                }
            }
        }
    }
}


@Composable
fun InputSetRow(
    modifier: Modifier = Modifier,
    set: SetSessionModel,
    cyclicColorManager: CyclicColorManager, // Use CyclicColorManager to fetch colors
    onWeightChange: (String) -> Unit,
    onRepsChange: (Int) -> Unit,
    weightMetric: WeightMetricTypes = WeightMetricTypes.LB,
    position: Int = 0,
    isFinished: Boolean = false
) {
    val backgroundColor = cyclicColorManager.nextColorByIndex(set.orderPosition) // Get the next color in the cycle

    val weightMetricText = when (weightMetric) {
        WeightMetricTypes.LB -> "lb"
        WeightMetricTypes.KG -> "kg"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(4.dp), // Padding applied first

        verticalAlignment = Alignment.CenterVertically
    ) {
        InputItem(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 8.dp),
            label = "WEIGHT",
            icon = LedgerIcons.Custom.WeightBarIcon(),
            placeholder = weightMetricText,
            onValueChange = {
                onWeightChange(it)
            },
            isFinished = isFinished,
            userInput = set.weight?.toString() ?: "0",
            position = set.orderPosition
        )
        Spacer(modifier = Modifier.height(8.dp))
        InputItem(
            modifier = Modifier.padding(start = 8.dp),
            label = "REPS",
            icon = LedgerIcons.Custom.MuscleIcon(),
            placeholder = "0",
            onValueChange = {
                onRepsChange(it.toInt())
            },
            isFinished = isFinished,
            userInput = set.reps?.toString() ?: "0",
            position = set.orderPosition
        )
    }
}


@Composable
fun InputItem(
    label: String,
    icon: ImageVector,
    placeholder: String,
    userInput: String?,
    onValueChange: (String) -> Unit,
    isFinished: Boolean = false,
    modifier: Modifier = Modifier,
    position: Int
) {
    var text by remember { mutableStateOf(userInput?.toString() ?: "0") }
    val focusManager = LocalFocusManager.current


    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clearAllFocusOnTap(focusManager),
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
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        EditableSetTextField(
            text = text,
            isFinished = isFinished,
            onRepsChange = { value ->
                text = value
                onValueChange(value)
            },
            onIntent = { position, text ->
                onValueChange(text)
            },
            position = position
        )

//                OutlinedTextField(
//                value = text,
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Number
//                ),
//                onValueChange = {
//                    onValueChange(it)
//                    text = it
//                },
//                placeholder = { Text(text = placeholder) },
//                modifier = Modifier.weight(1f).height(32.dp)
//            )
    }

    Spacer(modifier = Modifier.width(8.dp))
}

class CyclicColorManager(baseColor: Color, private val cycleSize: Int = 4) {
    private val colors: List<Color>
    private var currentIndex = 0

    init {
        // Generate a list of colors by adjusting the alpha of the base color
        colors = List(cycleSize) { index ->
            val alphaFactor = 1f - (index.toFloat() / cycleSize) // Start darker, progress lighter
            baseColor.copy(alpha = alphaFactor)
        }
    }

    /**
     * Gets the next color in the cycle.
     */
    fun nextColor(): Color {
        val color = colors[currentIndex]
        currentIndex = (currentIndex + 1) % colors.size
        return color
    }

    fun nextColorByIndex(index:Int): Color {
        val color = colors[index%colors.size]
        return color
    }



    /**
     * Resets the cycle to start from the beginning.
     */
    fun reset() {
        currentIndex = 0
    }
}


@Preview(showBackground = true)
@Composable
fun MachineTypeSectionPreview() {
    WorkoutSessionTheme {
        ExerciseItemCard2(
            onWeightChange = { weight, position -> },
            onRepsChange = { reps, position -> },
            weightMetric = WeightMetricTypes.LB,
            exercise = ExerciseSessionModel(
                orderPosition = 0,
                sets = listOf(
                    SetSessionModel(
                        orderPosition = 0,
                        weight = 100f,
                        reps = 10
                    ),
                    SetSessionModel(
                        orderPosition = 1,
                        weight = 100f,
                        reps = 10
                    ),
                ),
                exercise = null
            ),
            onSetAdded = {

            },
            isFinished = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseItemCardPreviewFinished() {
    WorkoutSessionTheme {
        ExerciseItemCard2(
            onWeightChange = { weight, position -> },
            onRepsChange = { reps, position -> },
            weightMetric = WeightMetricTypes.LB,
            exercise = ExerciseSessionModel(
                orderPosition = 0,
                sets = listOf(
                    SetSessionModel(
                        orderPosition = 0,
                        weight = 100f,
                        reps = 10
                    ),
                    SetSessionModel(
                        orderPosition = 1,
                        weight = 100f,
                        reps = 10
                    ),
                ),
                exercise = null
            ),
            onSetAdded = {

            },
            isFinished = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputRowPreview() {
    WorkoutSessionTheme {
        InputItem(
            label = "SET",
            icon = LedgerIcons.Custom.LiftingLedgerIcon(),
            placeholder = "Enter Set",
            onValueChange = {},
            userInput = "0",
            isFinished = false,
            position = 0
        )
    }
}


