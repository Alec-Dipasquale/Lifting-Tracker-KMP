package com.squalec.liftingtracker.android.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    object Loading

    @Serializable
    object Home

    @Serializable
    object ExerciseSearch

    @Serializable
    data class ExerciseDetail(
        val exerciseId: String
    )

    @Serializable
    data class WorkoutSession(
        val date: String? = null
    )
}