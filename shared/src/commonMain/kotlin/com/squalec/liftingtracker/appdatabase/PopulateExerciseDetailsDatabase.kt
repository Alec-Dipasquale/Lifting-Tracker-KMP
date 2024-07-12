package com.squalec.liftingtracker.appdatabase

import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun parseExercises(jsonString:String):List<ExerciseDetails> {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(jsonString)
}

