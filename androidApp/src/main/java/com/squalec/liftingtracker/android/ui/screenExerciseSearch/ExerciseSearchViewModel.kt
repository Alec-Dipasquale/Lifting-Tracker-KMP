package com.squalec.liftingtracker.android.ui.screenExerciseSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseDetailsRepository
import com.squalec.liftingtracker.appdatabase.repositories.Operator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


class ExerciseSearchViewModel (private val exerciseDetailsRepository: ExerciseDetailsRepository) : ViewModel() {

    private val _state = MutableStateFlow(ExerciseSearchState())
    val state: StateFlow<ExerciseSearchState> get()  = _state

    init {
        intent(ExerciseSearchIntent.GetAllMuscleNames)
    }

    fun intent(intent: ExerciseSearchIntent) {
        when (intent) {
            is ExerciseSearchIntent.GetAllMuscleNames -> getMuscleNames()
            is ExerciseSearchIntent.SearchExercises -> searchExercises(intent.searchText)
            is ExerciseSearchIntent.UpdateFilter -> updateFilter(intent.filter)
            is ExerciseSearchIntent.OperatorChanged -> updateOperator(intent.operator)
        }
    }

    private fun updateOperator(operator: Operator) {
        _state.update {
            it.copy(
                muscleFilterState = it.muscleFilterState.copy(
                    operatorSelected = operator
                )
            )
        }
    }

    private fun getMuscleNames() {
        _state.update {
            it.copy(
                isExerciseLoading = true
            )
        }
        viewModelScope.launch {
            val muscleNames = exerciseDetailsRepository.muscleNames()
            Timber.d("Muscle names: $muscleNames")
            _state.update {
                it.copy(
                    isExerciseLoading = false,
                    muscleNames = muscleNames
                )
            }
        }
        searchExercises("")
    }

    private fun searchExercises(
        searchText: String,
        muscle: List<String>? = null,
        equipment: String? = null,
        level: String? = null,
        force: String? = null,
        mechanic: String? = null,
        category: String? = null
    ) {
        _state.update {
            it.copy(
                searchText = searchText,
            )
        }

        viewModelScope.launch {
            val exercises = exerciseDetailsRepository.searchExercisesWithFilters(
                searchText,
                muscle?: emptyList(),
                equipment?: "",
                level?: "",
                force?: "",
                mechanic?: "",
                category?: "",
                operatorFilter = _state.value.muscleFilterState.operatorSelected
            )

            _state.update {
                it.copy(
                    exercises = exercises
                )
            }
        }
    }


    private fun updateFilter(filter: ExerciseFilters) {
        val tempFilter = _state.value.filters

        val updatedFilter = tempFilter.copy(
            muscle = filter.muscle ?: tempFilter.muscle,
            equipment = filter.equipment ?: tempFilter.equipment,
            level = filter.level ?: tempFilter.level,
            force = filter.force ?: tempFilter.force,
            mechanic = filter.mechanic ?: tempFilter.mechanic,
            category = filter.category ?: tempFilter.category
        )

        _state.update {
            it.copy(
                filters = updatedFilter
            )
        }

        searchExercises(
            _state.value.searchText,
            updatedFilter.muscle,
            updatedFilter.equipment,
            updatedFilter.level,
            updatedFilter.force,
            updatedFilter.mechanic,
            updatedFilter.category
        )
    }


}

sealed class ExerciseSearchIntent {
    data class SearchExercises(val searchText: String) : ExerciseSearchIntent()
    data class UpdateFilter(val filter: ExerciseFilters) : ExerciseSearchIntent()
    data object GetAllMuscleNames : ExerciseSearchIntent()
    data class OperatorChanged(val operator: Operator) : ExerciseSearchIntent()
}

data class ExerciseSearchState(
    val isExerciseLoading: Boolean = false,
    val exercises: List<ExerciseDetails> = emptyList(),
    val filters: ExerciseFilters = ExerciseFilters(),
    val muscleFilterState: MuscleFilterState = MuscleFilterState(),
    val muscleNames: List<String>? = null,
    val searchText: String = ""
)

data class MuscleFilterState(
    val operatorSelected: Operator = Operator.AND,
)


data class ExerciseFilters(
    val muscle: List<String>? = null,
    val equipment: String? = null,
    val level: String? = null,
    val force: String? = null,
    val mechanic: String? = null,
    val category: String? = null
)