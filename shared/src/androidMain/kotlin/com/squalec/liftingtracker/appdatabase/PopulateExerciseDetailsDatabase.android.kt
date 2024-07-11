package com.squalec.liftingtracker.appdatabase

import android.content.Context
import android.util.Log
import androidx.room.Room
import org.koin.java.KoinJavaComponent.getKoin

actual suspend fun populateExerciseDetailsDatabase() {
    val context: Context = getKoin().get()
    val jsonString = loadJsonFromAssets(context, "exercises.json")
    val exercises = parseExercises(jsonString)
    Log.d("[Database123]", "Exercises: $exercises")
    val db = DBFactory.createDatabase()
    db.exerciseDao().insertExercises(*exercises.toTypedArray())
}

fun loadJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}