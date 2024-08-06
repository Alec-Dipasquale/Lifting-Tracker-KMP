package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squalec.liftingtracker.appdatabase.models.UserExercise
import com.squalec.liftingtracker.appdatabase.models.UserSet
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionRepository
import com.squalec.liftingtracker.utils.CustomDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutSessionViewModel (private val exerciseDetailsRepository: WorkoutSessionRepository): ViewModel(){

    private val _state = MutableStateFlow(WorkoutSessionState(CustomDate.now(), null))
    val state: StateFlow<WorkoutSessionState> = _state

    fun handleEvent(event: WorkoutSessionEvent){
        when(event){
            is WorkoutSessionEvent.OnAddExercise -> {
                // Add exercise to workout session
            }
            is WorkoutSessionEvent.OnRemoveExercise -> {
                // Remove exercise from workout session
            }
            is WorkoutSessionEvent.OnChangeDate -> {
                changeDate(event.date)
            }
            is WorkoutSessionEvent.OnAddSet -> TODO()
            is WorkoutSessionEvent.OnRemoveSet -> TODO()
        }
    }

    private fun changeDate(date: CustomDate){
        _state.update {
            WorkoutSessionState(
                date,
                )
        }
    }

    private fun loadWorkoutByDate(date: CustomDate){
        viewModelScope.launch(Dispatchers.IO) {
            val result = exerciseDetailsRepository.getWorkoutSessionByDate(date)
            _state.update {
                it.copy(
                    workoutSessionModel = result
                )
            }
        }
    }
}

data class WorkoutSessionState(
    val date: CustomDate,
    val workoutSessionModel: WorkoutSessionModel? = null
)

sealed class WorkoutSessionEvent {
    data class OnChangeDate(val date: CustomDate): WorkoutSessionEvent()
    data class OnAddExercise(val exercise: UserExercise): WorkoutSessionEvent()
    object OnRemoveExercise: WorkoutSessionEvent()
    object OnRemoveSet: WorkoutSessionEvent()
    data class OnAddSet(val set: UserSet): WorkoutSessionEvent()
}