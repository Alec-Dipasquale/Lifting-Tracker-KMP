package com.squalec.liftingtracker.android.ui.utilities

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clearAllFocusOnTap(focusManager: FocusManager) = this.pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }