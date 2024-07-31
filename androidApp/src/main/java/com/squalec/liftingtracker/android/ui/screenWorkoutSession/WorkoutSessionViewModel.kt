package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkoutSessionViewModel: ViewModel(){

    private val _state = MutableStateFlow(WorkoutSessionState("-1", listOf()))
    val state: StateFlow<WorkoutSessionState> = _state

    fun handleEvent(event: WorkoutSessionEvent){
        when(event){
            is WorkoutSessionEvent.OnSave -> {
                // Save workout session
            }
            is WorkoutSessionEvent.OnCancel -> {
                // Cancel workout session
            }
            is WorkoutSessionEvent.OnAddExercise -> {
                // Add exercise to workout session
            }
            is WorkoutSessionEvent.OnRemoveExercise -> {
                // Remove exercise from workout session
            }
        }
    }

}

data class WorkoutSessionState(
    val date: String,
    val exercises: List<Any>
)

sealed class WorkoutSessionEvent {
    object OnSave: WorkoutSessionEvent()
    object OnCancel: WorkoutSessionEvent()
    data class OnAddExercise(val exercise: Any): WorkoutSessionEvent()
    object OnRemoveExercise: WorkoutSessionEvent()
}