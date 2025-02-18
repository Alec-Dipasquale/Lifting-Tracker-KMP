package com.squalec.liftingtracker.android.ui.utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.squalec.liftingtracker.R

object Icons {
    object Custom {
        @Composable
        fun MyHistory() = ImageVector.vectorResource(id = com.squalec.liftingtracker.android.R.drawable.history_icon)

    }
}