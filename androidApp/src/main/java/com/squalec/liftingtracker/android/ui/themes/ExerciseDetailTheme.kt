package com.squalec.liftingtracker.android.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
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
    val localMyColorScheme = staticCompositionLocalOf {
        MyColorScheme(
            lightColorScheme,
            bannerContainerColor
        )
    }

    val colors = if (darkTheme) {
        myColorSchemeDark
    } else {
        myColorSchemeLight
    }
    val typography = defaultTypography

    val shapes = defaultShape


    CompositionLocalProvider(localMyColorScheme provides colors) {

        MaterialTheme(
            colorScheme = colors.materialColorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}