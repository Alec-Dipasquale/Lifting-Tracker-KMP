package com.squalec.liftingtracker.android.ui.screenCalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.squalec.liftingtracker.android.ui.navigation.Destination
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionModel
import com.squalec.liftingtracker.appdatabase.repositories.WorkoutSessionRepository
import com.squalec.liftingtracker.utils.CustomDate
import com.squalec.liftingtracker.utils.CustomDateRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalendarViewModel(private val workoutSessionRepository: WorkoutSessionRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(CalendarState())
    val state: StateFlow<CalendarState> get() = _state

    fun intent(intent: CalendarIntent) {
        when (intent) {
            is CalendarIntent.GetWorkoutSessions -> getWorkoutSessions(intent.dateRange)
            is CalendarIntent.OnDaySelected -> showWorkoutsDialogue(intent.day, intent.workoutSessions)
            is CalendarIntent.CloseWorkoutsDialogue -> closeWorkoutsDialogue()
            is CalendarIntent.OnWorkoutSelected -> workoutSelected(intent.workoutSession, intent.navController)
        }
    }

    private fun getWorkoutSessions(dateRange: CustomDateRange) {
        _state.value = CalendarState(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = workoutSessionRepository.getWorkoutSessionByRange(dateRange = dateRange)
            _state.update {
                it.copy(workoutSessions = result, loading = false)
            }
        }
    }

    private fun showWorkoutsDialogue(day: CustomDate, workoutSessions: List<WorkoutSessionModel>) {
        _state.update {
            it.copy(calendarWorkoutDialogueState = CalendarWorkoutDialogueState(workoutSessions, day, true))
        }
    }

    private fun closeWorkoutsDialogue() {
        _state.update {
            it.copy(calendarWorkoutDialogueState = CalendarWorkoutDialogueState())
        }
    }

    private fun workoutSelected(workoutSession: WorkoutSessionModel, nav: NavController) {
        nav.navigate(Destination.WorkoutSession(workoutSessionID = workoutSession.workoutId)) {
            popUpTo(Destination.WorkoutSession()) {
                inclusive = true
            }
        }
    }

}

data class CalendarState(
    val workoutSessions: List<WorkoutSessionModel> = emptyList(),
    val calendarWorkoutDialogueState: CalendarWorkoutDialogueState = CalendarWorkoutDialogueState(),
    val loading: Boolean = false,
    val error: String = ""

)

data class CalendarWorkoutDialogueState(
    val workoutSessions: List<WorkoutSessionModel> = emptyList(),
    val day: CustomDate = CustomDate.now(),
    val isWorkoutsDialogueOpen: Boolean = false
)

sealed class CalendarIntent {
    data class GetWorkoutSessions(val dateRange: CustomDateRange) : CalendarIntent()
    data class OnDaySelected(val day: CustomDate, val workoutSessions: List<WorkoutSessionModel>) : CalendarIntent()
    object CloseWorkoutsDialogue : CalendarIntent()
    data class OnWorkoutSelected(val workoutSession: WorkoutSessionModel, val navController: NavController) : CalendarIntent()
}