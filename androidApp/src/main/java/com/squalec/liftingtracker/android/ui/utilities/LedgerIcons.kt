package com.squalec.liftingtracker.android.ui.utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

object LedgerIcons {
    object Custom {
        @Composable
        fun MyHistory() = ImageVector.vectorResource(id = com.squalec.liftingtracker.android.R.drawable.history_icon)
        @Composable
        fun WeightBarIcon() = ImageVector.vectorResource(id = com.squalec.liftingtracker.android.R.drawable.weight_dumbell_icon)
        @Composable
        fun MuscleIcon() = ImageVector.vectorResource(id = com.squalec.liftingtracker.android.R.drawable.muscle_icon)
        @Composable
        fun LiftingLedgerIcon() = ImageVector.vectorResource(id = com.squalec.liftingtracker.android.R.drawable.lifting_ledger_icon)
        @Composable
        fun DefaultBackground() = ImageVector.vectorResource(id = com.squalec.liftingtracker.android.R.drawable.lifting_ledger_moblie_pattern_01)

    }
}