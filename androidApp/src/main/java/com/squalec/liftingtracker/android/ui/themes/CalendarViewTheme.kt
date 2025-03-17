package com.squalec.liftingtracker.android.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf



@Composable
fun CalendarViewTheme(
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