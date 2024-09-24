package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
                exercise = exercise,
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
                exercise = exercise,
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
    onIntent: (position:Int, text:String) -> Unit,
    exercise: ExerciseSessionModel, // Assuming this is your Exercise data class or type
    set: SetSessionModel // Assuming this is your Set data class or type
) {


    var textFieldValue by remember { mutableStateOf(TextFieldValue(text)) }
    var isFirstFocus by remember { mutableStateOf(false) }

    if (isFinished) {
        Text(
            text = textFieldValue.text,
            modifier = Modifier
                .width(90.dp)
                .clip(shape = RoundedCornerShape(4.dp)),
            style = MaterialTheme.typography.bodyMedium // Adjust style as needed
        )
    } else {
        TextField(
            modifier = Modifier
                .width(90.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        textFieldValue = textFieldValue.copy(
                            selection = TextRange(0, 0)
                        )
                        onIntent(
                            set.orderPosition, textFieldValue.text
                        )
                    } else{
                        textFieldValue = textFieldValue.copy(
                            selection = TextRange(0, textFieldValue.text.length)
                        )
                        isFirstFocus = true
                    }
                },
            value = textFieldValue,
            onValueChange = { newValue ->
                if (isFirstFocus) {
                    textFieldValue = newValue.copy(
                        text = newValue.text,
                        selection = TextRange(0, newValue.text.length)
                    )
                    isFirstFocus = false

                } else {
                    textFieldValue = newValue.copy(
                        text = newValue.text,
                        selection = newValue.selection
                    )
                    onIntent(
                        set.orderPosition, newValue.text
                    )
                }
                onRepsChange(newValue.text)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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

