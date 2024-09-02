package com.squalec.liftingtracker.appdatabase


import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.utils.CustomDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object WorkoutSessionManager {

    private val _workoutState = MutableStateFlow(
        WorkoutSessionManagedState(
            isWorkoutInProgress = false
        )
    )

    val workoutState: StateFlow<WorkoutSessionManagedState> get() = _workoutState

    private val _workoutStartTime = MutableStateFlow<Instant?>(null)

    fun startWorkout() {
        _workoutState.update {
            it.copy(
                isWorkoutInProgress = true,
                date = CustomDate.now()
                )
        }
        _workoutStartTime.value = Clock.System.now()
    }

    fun stopWorkout() {
        _workoutState.update {
            it.copy(isWorkoutInProgress = false)
        }
        _workoutStartTime.value = null
        resetWorkoutSessionModel()
    }

    fun getWorkoutDuration(): Long {
        return _workoutStartTime.value?.let { start ->
            Clock.System.now().epochSeconds - start.epochSeconds
        } ?: 0
    }

    fun updateModel(workoutSessionModel: WorkoutSessionModel) {
        _workoutState.update {
            it.copy(workoutSessionModel = workoutSessionModel)
        }
    }

    private fun resetWorkoutSessionModel() {
        _workoutState.update {
            it.copy(workoutSessionModel = WorkoutSessionModel(
                date = CustomDate.now(),
                caloriesBurned = 0,
                duration = 0,
                exercises = emptyList()
            ))
        }
    }

    fun getWorkoutSessionModel(): WorkoutSessionModel {
        return _workoutState.value.workoutSessionModel
    }

}

data class WorkoutSessionManagedState(
    val workoutSessionModel: WorkoutSessionModel = WorkoutSessionModel(
        date = CustomDate.now(),
        caloriesBurned = 0,
        duration = 0,
        exercises = emptyList()
    ),
    val isWorkoutInProgress: Boolean = false,
    val date: CustomDate = CustomDate.now()
)

