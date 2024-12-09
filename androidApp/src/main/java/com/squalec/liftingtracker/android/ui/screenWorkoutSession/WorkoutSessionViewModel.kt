package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squalec.liftingtracker.appdatabase.Logs
import com.squalec.liftingtracker.appdatabase.WorkoutSessionManager
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
        Logs().debug("Workout Session ViewModel initialized")
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
//                createWorkoutId()
            }

            is WorkoutSessionEvent.OnFinishedWorkout -> {
                finishWorkout()
            }

            is WorkoutSessionEvent.OnAddSet -> {
                onAddSet(event.set, event.exerciseId)
            }

            is WorkoutSessionEvent.OnRemoveSet -> {

            }

            is WorkoutSessionEvent.ChangeSetWeight -> {
                onChangeSetWeight(event.exercise, event.position, event.weight)
            }

            is WorkoutSessionEvent.ChangeSetReps -> {
                onChangeSetReps(event.exercise, event.position, event.reps)
            }

            is WorkoutSessionEvent.ChangeWorkoutTitle -> {
                changeWorkoutTitle(event.text)
            }

            is WorkoutSessionEvent.OnLoadWorkoutSession -> onLoadWorkoutSession(event.workoutId)
        }
    }

//    private fun createWorkoutId() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val workoutId = workoutSessionRepository.
//            _state.update {
//                it.copy(
//                    workoutSessionModel = it.workoutSessionModel?.copy(
//                        workoutId = workoutId
//                    )
//                )
//            }
//        }
//    }

    private fun onChangeSetReps(exercise: ExerciseSessionModel, position: Int, reps: Int) {
        Logs().debug("Changing reps for exercise: ${exercise.exercise?.name}")
        val updatedExercise = exercise.copy(
            sets = exercise.sets.map {
                if (it.orderPosition == position) {
                    it.copy(
                        reps = reps
                    )
                } else {
                    it
                }
            }
        )
        _state.update {
            it.copy(
                workoutSessionModel = it.workoutSessionModel?.copy(
                    exercises = it.workoutSessionModel.exercises.map {
                        if (it == exercise) {
                            updatedExercise
                        } else {
                            it
                        }
                    }
                )
            )
        }
        onUpdateWorkoutCache()

    }

    private fun onChangeSetWeight(exercise: ExerciseSessionModel, position: Int, weight: Float) {
        Logs().debug("Changing weight to $weight for exercise: ${exercise.exercise?.name}")
        val updatedExercise = exercise.copy(
            sets = exercise.sets.map {
                if (it.orderPosition == position) {
                    it.copy(
                        weight = weight
                    )
                } else {
                    it
                }
            }
        )
        _state.update {
            it.copy(
                workoutSessionModel = it.workoutSessionModel?.copy(
                    exercises = it.workoutSessionModel.exercises.map {
                        if (it == exercise) {
                            updatedExercise
                        } else {
                            it
                        }
                    }
                )
            )
        }
        onUpdateWorkoutCache()

    }

    private fun finishWorkout() {
        Logs().debug("Finishing workout")
        WorkoutSessionManager.stopWorkout()
        var workoutSession = _state.value.workoutSessionModel
        workoutSession = workoutSession?.copy(duration = WorkoutSessionManager.getWorkoutDuration())

        viewModelScope.launch(Dispatchers.IO) {
            workoutSessionRepository.saveWorkoutSession(
                workoutSession ?: return@launch
            )
        }

        _state.update {
            it.copy(
                isFinished = true
            )
        }
    }

    private fun onAddSet(set: SetSessionModel, exerciseId: String) {
        Logs().debug("Adding set to exercise: $exerciseId")
        val workoutSession = _state.value.workoutSessionModel ?: return
        val exercise = workoutSession.exercises.find { it.exercise?.id == exerciseId } ?: return
        val lastSet = exercise.sets.lastOrNull() ?: return
        val newSet = lastSet.copy(
            orderPosition = exercise.sets.size
        )
        _state.update { workoutSessionState ->
            workoutSessionState.copy(
                workoutSessionModel = workoutSession.copy(
                    exercises = workoutSession.exercises.map { exerciseSessionModel ->
                        if (exerciseSessionModel == exercise) {
                            exerciseSessionModel.copy(
                                sets = exerciseSessionModel.sets.plus(newSet)
                            )
                        } else {
                            exerciseSessionModel
                        }
                    }
                )
            )
        }
        onUpdateWorkoutCache()
    }

    private fun onAddExercise(exerciseId: String) {
        Logs().debug("Adding exercise: $exerciseId")
        Logs().debug("Workout Session date: ${_state.value.date?.displayFormat()}")
        val isDuplicate =
            _state.value.workoutSessionModel?.exercises?.any { it.exercise?.id == exerciseId }
                ?: false
        if (isDuplicate) {
            Logs().debug("Exercise already added")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val exerciseDetail = exerciseDetailsRepository.getExerciseDetails(exerciseId)
            Logs().debug("Fetched exercise details for ${exerciseDetail?.name}")

            // Additional log before updating the state
            Logs().debug(
                "State before update: exercise_id ${_state.value.workoutSessionModel?.workoutId}, " +
                        "exercises: ${_state.value.workoutSessionModel?.exercises?.joinToString { it.exercise?.name ?: "Unknown" }}"
            )

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
            Logs().debug("Added exercise: ${exerciseDetail?.name}")
            Logs().debug(
                "State after update: exercise_id ${_state.value.workoutSessionModel?.workoutId}, " +
                        "exercises: ${_state.value.workoutSessionModel?.exercises?.joinToString { it.exercise?.name ?: "Unknown" }}"
            )

        }
        onUpdateWorkoutCache()
    }

    private fun changeDate(date: CustomDate) {
        Logs().debug("Changing date to ${date.utcDate}")
        _state.update { currentState ->
            currentState.copy(
                date = date,
                workoutSessionModel = WorkoutSessionModel(
                    date = date,
                    exercises = emptyList()
                )
            )
        }
        onUpdateWorkoutCache()
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

    private fun onUpdateWorkoutCache() {
        Logs().debug("Updating Workout Session Manager cache")
        WorkoutSessionManager.updateModel(_state.value.workoutSessionModel ?: return)
    }

    private fun changeWorkoutTitle(text: String) {
        Logs().debug("Changing workout title to $text from ${_state.value.workoutSessionModel?.workoutName}")
        _state.update {
            it.copy(
                workoutSessionModel = it.workoutSessionModel?.copy(
                    workoutName = text
                )
            )
        }
        onUpdateWorkoutCache()
    }

    private fun onLoadWorkoutSession(workoutId: String) {
        Logs().debug("Loading workout session with ID: $workoutId")
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val workoutSession = workoutSessionRepository.getWorkoutSessionById(workoutId)
            _state.update {
                it.copy(
                    workoutSessionModel = WorkoutSessionModel(
                        workoutId = workoutSession.workoutId,
                        workoutName = workoutSession.workoutName,
                        exercises = workoutSession.exercises,
                        date = workoutSession.date

                    ),
                    date = workoutSession.date,
                    isFinished = true,
                    isLoading = false
                )
            }
        }
    }
}

data class WorkoutSessionState(
    val date: CustomDate? = null,
    val workoutSessionModel: WorkoutSessionModel? = null,
    val isFinished: Boolean = false,
    val isLoading : Boolean = false
)

sealed class WorkoutSessionEvent {
    data class OnChangeDate(val date: CustomDate) : WorkoutSessionEvent()
    data class OnAddExercise(val exerciseId: String) : WorkoutSessionEvent()
    object OnRemoveExercise : WorkoutSessionEvent()
    object OnRemoveSet : WorkoutSessionEvent()
    object OnFinishedWorkout : WorkoutSessionEvent()
    data class OnAddSet(val set: SetSessionModel, val exerciseId: String) : WorkoutSessionEvent()
    data class ChangeSetWeight(
        val exercise: ExerciseSessionModel,
        val position: Int,
        val weight: Float
    ) : WorkoutSessionEvent()

    data class ChangeSetReps(val exercise: ExerciseSessionModel, val position: Int, val reps: Int) :
        WorkoutSessionEvent()

    data class ChangeWorkoutTitle(val text: String) : WorkoutSessionEvent()
    data class OnLoadWorkoutSession(val workoutId: String) : WorkoutSessionEvent()
}