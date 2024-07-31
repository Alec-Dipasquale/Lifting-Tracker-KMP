package com.squalec.liftingtracker.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text

@Composable
fun ExerciseImage(drawable: ImageBitmap?) {
    if (drawable != null) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f) // Adjust the aspect ratio as needed
                .clip(RoundedCornerShape(8.dp)),
            bitmap = drawable,
            contentDescription = null
        )
    } else {
        Text(text = "Image not found")
        // Handle the case where the image is not found
        // You can use a placeholder image or show an error message
    }
}