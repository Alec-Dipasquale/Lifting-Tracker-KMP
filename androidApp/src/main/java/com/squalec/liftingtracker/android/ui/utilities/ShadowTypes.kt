package com.squalec.liftingtracker.android.ui.utilities

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ShadowTypes {
    data class ShadowParams(
        val offsetX: Dp = 2.dp,
        val offsetY: Dp = 2.dp,
        val blurRadius: Dp = 4.dp,
        val cornerRadius: Dp = 4.dp,
        val color: Color = Color.Black
    )
    val large = ShadowParams()
    val small = ShadowParams(
        offsetX = 0.dp,
        offsetY = 0.dp,
        blurRadius = 1.dp,
        cornerRadius = 1.dp
    )
    val medium = ShadowParams(
        offsetX = 2.dp,
        offsetY = 2.dp,
        blurRadius = 4.dp,
        cornerRadius = 8.dp
    )
}