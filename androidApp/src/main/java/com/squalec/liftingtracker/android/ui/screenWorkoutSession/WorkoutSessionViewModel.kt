package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManager
import com.squalec.liftingtracker.appdatabase.models.UserSet
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseDetailsRepository
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.SetSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionRepository
import com.squalec.liftingtracker.utils.CustomDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class WorkoutSessionViewModel(
    private val workoutSessionRepository: WorkoutSessionRepository,
    private val exerciseDetailsRepository: ExerciseDetailsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutSessionState(null, null))
    val state: StateFlow<WorkoutSessionState> = _state

    init {
        Timber.d("WorkoutSessionViewModel initialized")
        val workoutState = WorkoutSessionManager.workoutState.value
        if (workoutState.isWorkoutInProgress) {
            _state.update {
                it.copy(
                    workoutSessionModel = workoutState.workoutSessionModel,
                    date = workoutState.date
                )
            }
        }
    }

    fun handleEvent(event: WorkoutSessionEvent) {
        when (event) {
            is WorkoutSessionEvent.OnAddExercise -> {
                // Add exercise to workout session
                onAddExercise(event.exerciseId)
            }

            is WorkoutSessionEvent.OnRemoveExercise -> {
                // Remove exercise from workout session
            }

            is WorkoutSessionEvent.OnChangeDate -> {
                changeDate(event.date)
                WorkoutSessionManager.startWorkout()
            }

            is WorkoutSessionEvent.OnFinishedWorkout -> {
                finishWorkout()
            }

            is WorkoutSessionEvent.OnUpdateWorkout -> {
                onUpdateWorkout(event.workoutSessionModel)
            }

            is WorkoutSessionEvent.OnAddSet -> {
                onAddSet()
            }
            is WorkoutSessionEvent.OnRemoveSet -> TODO()
        }
    }

    private fun finishWorkout() {
        WorkoutSessionManager.stopWorkout()
        var workoutSession = _state.value.workoutSessionModel
        workoutSession = workoutSession?.copy(duration = WorkoutSessionManager.getWorkoutDuration())
        viewModelScope.launch(Dispatchers.IO) {
            workoutSessionRepository.saveWorkoutSession(
                workoutSession ?: return@launch
            )
        }
    }

    private fun onAddSet() {
        val workoutSession = _state.value.workoutSessionModel ?: return
        val lastExercise = workoutSession.exercises.lastOrNull() ?: return
        val lastSet = lastExercise.sets.lastOrNull() ?: return
        val newSet = lastSet.copy(
            orderPosition = lastExercise.sets.size
        )
        _state.update {
            it.copy(
                workoutSessionModel = workoutSession.copy(
                    exercises = workoutSession.exercises.map {
                        if (it == lastExercise) {
                            it.copy(
                                sets = it.sets.plus(newSet)
                            )
                        } else {
                            it
                        }
                    }
                )
            )
        }
    }

    private fun onUpdateWorkout(workoutSessionModel: WorkoutSessionModel) {
        _state.update {
            it.copy(
                workoutSessionModel = workoutSessionModel
            )
        }
    }

    private fun onAddExercise(exerciseId: String) {
        Timber.d("Adding exercise: $exerciseId")
        Timber.d("Workout session 1: ${_state.value.workoutSessionModel}")
        Timber.d("Workout Session date: ${_state.value.date?.displayFormat()}")
        val isDuplicate =
            _state.value.workoutSessionModel?.exercises?.any { it.exercise?.id == exerciseId }
                ?: false
        if (isDuplicate) {
            Timber.d("Exercise already added")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val exerciseDetail = exerciseDetailsRepository.getExerciseDetails(exerciseId)
            Timber.d("Fetched exercise details: $exerciseDetail")

            // Additional log before updating the state
            Timber.d("State before update: ${_state.value}")

            _state.update {
                it.copy(
                    workoutSessionModel = it.workoutSessionModel?.copy(
                        exercises = it.workoutSessionModel.exercises.plus(
                            ExerciseSessionModel(
                                exercise = exerciseDetail,
                                sets = listOf(SetSessionModel()),
                                orderPosition = it.workoutSessionModel.exercises.size
                            )
                        )
                    )
                )
            }

            WorkoutSessionManager.updateModel(_state.value.workoutSessionModel ?: return@launch)

            // Logs after state update
            Timber.d("Added exercise: $exerciseDetail")
            Timber.d("Workout session 2: ${_state.value.workoutSessionModel}")

        }
    }

    private fun changeDate(date: CustomDate) {
        Timber.d("Changing date to ${date.utcDate}")
        _state.update { currentState ->
            currentState.copy(
                date = date,
                workoutSessionModel = WorkoutSessionModel(
                    date = date,
                    exercises = emptyList()
                )
            )
        }
        Timber.d("Workout session 3: ${_state.value.workoutSessionModel}")
    }

    private fun loadWorkoutByDate(date: CustomDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = workoutSessionRepository.getWorkoutSessionByDate(date)
            _state.update {
                it.copy(
                    workoutSessionModel = result
                )
            }
        }
    }
}

data class WorkoutSessionState(
    val date: CustomDate? = null,
    val workoutSessionModel: WorkoutSessionModel? = null
)

sealed class WorkoutSessionEvent {
    data class OnChangeDate(val date: CustomDate) : WorkoutSessionEvent()
    data class OnAddExercise(val exerciseId: String) : WorkoutSessionEvent()
    object OnRemoveExercise : WorkoutSessionEvent()
    object OnRemoveSet : WorkoutSessionEvent()
    object OnFinishedWorkout : WorkoutSessionEvent()
    data class OnAddSet(val set: SetSessionModel) : WorkoutSessionEvent()
    data class OnUpdateWorkout(val workoutSessionModel: WorkoutSessionModel) : WorkoutSessionEvent()
}