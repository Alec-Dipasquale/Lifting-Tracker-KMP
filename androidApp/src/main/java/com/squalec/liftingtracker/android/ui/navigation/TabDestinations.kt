package com.squalec.liftingtracker.android.ui.navigation

import kotlinx.serialization.Serializable

sealed class TabDestinations {
    @Serializable
    object Main : TabDestinations()
    object Profile : TabDestinations()
    object UserStats : TabDestinations()
}