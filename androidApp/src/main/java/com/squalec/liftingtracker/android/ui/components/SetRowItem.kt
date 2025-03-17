package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squalec.liftingtracker.android.ui.screenWorkoutSession.WorkoutSessionEvent
import com.squalec.liftingtracker.android.ui.themes.WorkoutSessionTheme
import com.squalec.liftingtracker.android.ui.utilities.clearAllFocusOnTap
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.SetSessionModel

@Composable
fun SetRowItem(
    modifier: Modifier = Modifier,
    set: SetSessionModel,
    exercise: ExerciseSessionModel,
    onIntent: (WorkoutSessionEvent) -> Unit,
    isFinished: Boolean
) {

    val focusManager = LocalFocusManager.current

    var weight by remember {
        mutableStateOf(set.weight?.toString() ?: "0.0")
    }
    var reps by remember {
        mutableStateOf(set.reps?.toString() ?: "0")
    }

    Box(modifier = modifier
        .clearAllFocusOnTap(focusManager)
    ) {
        Row(
            modifier = Modifier
                .clearAllFocusOnTap(focusManager)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${set.orderPosition + 1}")
            EditableSetTextField(
                text = weight,
                isFinished = isFinished,
                onRepsChange = { value ->
                    weight = value
                               },
                onIntent = { position, text ->
                    onIntent(WorkoutSessionEvent.ChangeSetWeight(exercise, position, text.toFloat()))
                },
                set = set
            )
            Text(text = "x")

            EditableSetTextField(
                text = reps,
                isFinished = isFinished,
                onRepsChange = { value ->
                    reps = value
                },
                onIntent = { position, text ->
                    onIntent(WorkoutSessionEvent.ChangeSetReps(exercise, position, text.toInt()))
                },
                set = set
            )
        }
    }
}

@Composable
fun EditableSetTextField(
    text: String,
    isFinished: Boolean,
    onRepsChange: (String) -> Unit,
    onIntent: (position: Int, text: String) -> Unit,
    set: SetSessionModel? = null, // Assuming this is your Set data class or type
    position: Int? = null
) {

//    todo get rid of the auto highlight after first number typed which deletes upon second number typed

    var textFieldValue by remember { mutableStateOf(TextFieldValue(text)) }
    var isFirstFocus by remember { mutableStateOf(false) }

    val orderPosition = set?.orderPosition ?: position ?: 0

    if (isFinished) {
        Text(
            text = textFieldValue.text,
            modifier = Modifier
                .width(50.dp)
                .clip(shape = RoundedCornerShape(4.dp)),
            style = MaterialTheme.typography.bodyMedium, // Adjust style as needed,
            color = MaterialTheme.colorScheme.onSurface
        )
    } else {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue.copy(
                    text = newValue.text,
                    selection = if (isFirstFocus) {
                        TextRange(0, newValue.text.length)
                    } else {
                        newValue.selection
                    }
                )
                isFirstFocus = false
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .width(65.dp)
                .clip(RoundedCornerShape(4.dp))
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        // Highlight the entire text
                        textFieldValue = textFieldValue.copy(
                            selection = TextRange(0, textFieldValue.text.length)
                        )
                        isFirstFocus = true
                    } else {
                        // Clear selection when focus is lost
                        textFieldValue = textFieldValue.copy(
                            selection = TextRange(0, 0)
                        )
                        onIntent(orderPosition, textFieldValue.text)
                    }
                },
            singleLine = true
        )
    }
}



@Preview
@Composable
fun SetRowItemPreview() {
    WorkoutSessionTheme(darkTheme = false) {
        SetRowItem(
            set = SetSessionModel(),
            exercise = ExerciseSessionModel(exercise = null, sets = emptyList(), orderPosition = 0),
            onIntent = {},
            isFinished = false
        )
    }
}

