package com.squalec.liftingtracker.android.ui.themes

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

//nikkis colors
val darkBackground = Color(0xFF151531) // background
val darkText = Color(0xFF452e8c) // regular text
val bannerContainerColor = Color(0xFF4c5aa8) // banner color
val primaryButtonContainer = Color(0xFF55b9e9) // button and accents
val secondaryContainerColor = Color(0xFF9360A8) // secondary title color

val darkColorScheme = darkColorScheme(
    primary = primaryButtonContainer,
    secondary = secondaryContainerColor,
    background = darkBackground,
    surface = Color(0xFFECECEC),
    onSurface = darkText,
    onBackground = Color(0xFFFFFFFF),
)

val  lightColorScheme = lightColorScheme(
    primary = primaryButtonContainer,
    secondary = secondaryContainerColor,
    background = darkBackground,
    surface = Color(0xFFECECEC),
    onSurface = darkText,
    onBackground = Color(0xFFFFFFFF),
)

val myColorSchemeLight = MyColorScheme(
    materialColorScheme = lightColorScheme,
    bannerContainerColor = bannerContainerColor
)

val myColorSchemeDark = MyColorScheme(
    materialColorScheme = lightColorScheme,
    bannerContainerColor = bannerContainerColor
)

data class MyColorScheme(
    val materialColorScheme: ColorScheme,
    val bannerContainerColor: Color
)

