package com.squalec.liftingtracker.android.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    object LoadingDestination

    @Serializable
    object HomeDestination

    @Serializable
    object ExerciseSearchDestination

    @Serializable
    data class ExerciseDetailDestination(
        val exerciseId: String
    )
}