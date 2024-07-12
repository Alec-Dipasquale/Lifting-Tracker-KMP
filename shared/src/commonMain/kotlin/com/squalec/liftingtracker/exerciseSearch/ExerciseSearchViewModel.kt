package com.squalec.liftingtracker.exerciseSearch

import com.rickclephas.kmm.viewmodel.*
import kotlinx.coroutines.flow.StateFlow
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.squalec.liftingtracker.appdatabase.DBFactory
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


open class ExerciseSearchViewModel : KMMViewModel() {

    private val _state = MutableStateFlow(viewModelScope, ExerciseSearchState())

    @NativeCoroutinesState
    val state: StateFlow<ExerciseSearchState> = _state

    fun intent(intent: ExerciseSearchIntent) {
        when (intent) {
            is ExerciseSearchIntent.SearchExercises -> searchExercises(intent.searchText)
        }
    }

    private fun searchExercises(searchText: String) {
        _state.update {
            it.copy(
                searchText = searchText,
                isExerciseLoading = true
            )
        }

        val db = DBFactory.createDatabase()
        viewModelScope.coroutineScope.launch {
            val exercises = db.exerciseDao().searchExercises(searchText)
            _state.update {
                it.copy(
                    isExerciseLoading = false,
                    exercises = exercises
                )
            }
        }
    }


}

sealed class ExerciseSearchIntent {
    data class SearchExercises(val searchText: String) : ExerciseSearchIntent()
}

data class ExerciseSearchState(
    val isExerciseLoading: Boolean = false,
    val exercises: List<ExerciseDetails> = emptyList(),
    val searchText: String = ""
)