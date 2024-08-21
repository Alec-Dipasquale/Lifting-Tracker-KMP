package com.squalec.liftingtracker.android.ui.utilities

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

fun Modifier.customShadow(
    params: ShadowTypes.ShadowParams = ShadowTypes.large
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->

            val color = params.color
            val offsetX = params.offsetX
            val offsetY = params.offsetY
            val blurRadius = params.blurRadius
            val cornerRadius = params.cornerRadius
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = (BlurMaskFilter(
                    blurRadius.toPx(),
                    BlurMaskFilter.Blur.SOLID
                ))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            canvas.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
                radiusX = cornerRadius.toPx(), // Apply the corner radius
                radiusY = cornerRadius.toPx(),
            )
        }
    }
)