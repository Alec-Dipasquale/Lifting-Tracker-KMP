package com.squalec.liftingtracker.android.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ExerciseDetailTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF1D00C7),
            secondary = Color(0xFFF15A25),
            tertiary = Color(0xFF3700B3),
            background = Color(0xFF121212),
            surface = Color(0xFF121212),
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF0000FF),
            secondary = Color(0xFFFF9742),
            tertiary = Color(0xFF3700B3),
            background = Color(0xFFE0E0E0),
            surface = Color(0xFFE0E0E0),

        )
    }
    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color= colors.onSurface
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}