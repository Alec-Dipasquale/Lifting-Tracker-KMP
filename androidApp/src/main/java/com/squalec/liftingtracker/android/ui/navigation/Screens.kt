package com.squalec.liftingtracker.android.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoadingDestination

@Serializable
object HomeDestination

@Serializable
object ExerciseSearchDestination

@Serializable
data class ExerciseDetailDestination(val exerciseId: String)