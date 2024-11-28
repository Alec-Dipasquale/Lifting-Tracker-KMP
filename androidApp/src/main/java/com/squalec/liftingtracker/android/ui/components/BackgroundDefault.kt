package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import com.squalec.liftingtracker.android.ui.utilities.LedgerIcons

@Composable
fun BackgroundDefault(
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(backgroundColor),
    ) {
        Image(
            imageVector = LedgerIcons.Custom.DefaultBackground(),
            colorFilter = ColorFilter.tint(tint),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds // Stretches or fits the SVG
        )
        content()
    }
}