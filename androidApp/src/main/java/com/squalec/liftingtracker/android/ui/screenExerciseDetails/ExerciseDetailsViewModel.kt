package com.squalec.liftingtracker.android.ui.screenExerciseDetails

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squalec.liftingtracker.appdatabase.DBFactory
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseDetailsRepository
import com.squalec.liftingtracker.appdatabase.repositories.ExerciseDetailsRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import java.io.IOException

class ExerciseDetailsViewModel(private val exerciseDetailsRepository: ExerciseDetailsRepository): ViewModel() {

    private val _state = MutableStateFlow(ExerciseDetailsState())
    val state: StateFlow<ExerciseDetailsState> get() = _state

    fun intent(intent: ExerciseDetailsIntent) {
        when (intent) {
            is ExerciseDetailsIntent.GetExercise -> getExerciseDetails(intent.id)
        }
    }

    private fun getExerciseDetails(id: String) {
        viewModelScope.launch {
            val exerciseDetails = exerciseDetailsRepository.getExerciseDetails(id)
            val exerciseImageIds = exerciseDetails?.getImageBitmap()
            _state.update {
                ExerciseDetailsState(
                    exerciseDetails = exerciseDetails,
                    exerciseImageIds = exerciseImageIds ?: emptyList()
                )
            }
        }
    }
}

data class ExerciseDetailsState(
    val exerciseDetails: ExerciseDetails? = null,
    val exerciseImageIds: List<ImageBitmap?> = emptyList()
)

sealed class ExerciseDetailsIntent {
    data class GetExercise(val id: String) : ExerciseDetailsIntent()
}


fun ExerciseDetails.getImageBitmap(): List<ImageBitmap>? {
    val context: Context = getKoin().get()
    val listOfImageBitmaps: MutableList<ImageBitmap> = mutableListOf()
    this.images?.forEach {
        val imageBitmap = getExerciseImageFromRaw(context, it)
        if (imageBitmap != null) {
            listOfImageBitmaps += imageBitmap.asImageBitmap()
        }
    }
    return listOfImageBitmaps
}


fun getExerciseImageFromRaw(context: Context, exercisePath:String): Bitmap? {
    val assetManager = context.assets
    val fileName = "exercises/$exercisePath"
    return try {
        val inputStream = assetManager.open(fileName)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}