package com.squalec.liftingtracker.android.ui.screenWorkoutSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squalec.liftingtracker.appdatabase.DBFactory
import com.squalec.liftingtracker.appdatabase.models.UserExercise
import com.squalec.liftingtracker.appdatabase.models.UserSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//class WorkoutSessionViewModel: ViewModel(){
//
//    private val _state = MutableStateFlow(WorkoutSessionState("-1", listOf()))
//    val state: StateFlow<WorkoutSessionState> = _state
//
//    fun handleEvent(event: WorkoutSessionEvent){
//        when(event){
//            is WorkoutSessionEvent.OnAddExercise -> {
//                // Add exercise to workout session
//            }
//            is WorkoutSessionEvent.OnRemoveExercise -> {
//                // Remove exercise from workout session
//            }
//
//            is WorkoutSessionEvent.LoadWorkoutSessionByDate -> {
//                loadWorkoutByDate(event.date)
//            }
//            is WorkoutSessionEvent.OnAddSet -> TODO()
//            is WorkoutSessionEvent.OnRemoveSet -> TODO()
//        }
//    }
//
//    private fun loadWorkoutByDate(date: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            // Load workout session by date
//            val db = DBFactory.createDatabase()
//            val workoutSession = db.userWorkoutSessionDao().getWorkoutSessionByDate(date)
//            val exercises = db.userExerciseDao().getExercisesByWorkoutSessionId(workoutSession.id)
//            val userExercises = exercises.map { exercise ->
//                val sets = db.userSetDao().getSetsByExerciseId(exercise.id)
//                UserExercise(exercise.id, exercise.name, sets)
//            }
//        }
//    }
//
//}
//
//data class WorkoutSessionState(
//    val date: String,
//    val exercises: List<UserExercise>
//)
//
//sealed class WorkoutSessionEvent {
//    data class OnAddExercise(val exercise: UserExercise): WorkoutSessionEvent()
//    object OnRemoveExercise: WorkoutSessionEvent()
//    object OnRemoveSet: WorkoutSessionEvent()
//    data class OnAddSet(val set: UserSet): WorkoutSessionEvent()
//    data class LoadWorkoutSessionByDate(val date:String): WorkoutSessionEvent()
//}