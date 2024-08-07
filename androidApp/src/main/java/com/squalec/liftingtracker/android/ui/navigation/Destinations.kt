package com.squalec.liftingtracker.android.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    object Loading

    @Serializable
    object Home

    @Serializable
    data class ExerciseSearch(
        val isOnClickExerciseEnabled: Boolean = false
    )

    @Serializable
    data class ExerciseDetail(
        val exerciseId: String
    )

    @Serializable
    data class WorkoutSession(
        val date: String? = null,
        val addedExerciseId: String? = null
    )
}