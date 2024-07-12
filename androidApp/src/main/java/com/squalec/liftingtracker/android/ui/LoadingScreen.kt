package com.squalec.liftingtracker.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center) {
        CircularProgressIndicator()
    }
}