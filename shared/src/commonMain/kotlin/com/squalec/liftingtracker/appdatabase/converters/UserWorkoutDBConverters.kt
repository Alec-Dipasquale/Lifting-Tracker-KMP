package com.squalec.liftingtracker.appdatabase.converters

//import androidx.room.TypeConverter
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
//import com.squalec.liftingtracker.appdatabase.models.enums.MuscleGroupType
//import java.lang.reflect.Type
//
//internal class Converters {
//    private val gson = Gson()
//
//    // Converter for ExerciseDetails
//    @TypeConverter
//    fun fromExerciseDetailsToJson(value: ExerciseDetails?): String {
//        return gson.toJson(value)
//    }
//
//    @TypeConverter
//    fun fromJsonToExerciseDetails(value: String?): ExerciseDetails? {
//        return gson.fromJson(value, ExerciseDetails::class.java)
//    }
//
//    //Converter for Map<String, MuscleGroup>
//    @TypeConverter
//    fun fromStringToMuscleGroupMap(value: String?): Map<String, MuscleGroupType>? {
//        val mapType: Type = object : TypeToken<Map<String, MuscleGroupType>>() {}.type
//        return gson.fromJson(value, mapType)
//    }
//
//    @TypeConverter
//    fun fromMuscleGroupMapToString(map: Map<String, MuscleGroupType>?): String {
//        return gson.toJson(map)
//    }
//
//    @TypeConverter
//    fun fromStringListToString(value: List<String>?): String {
//        return gson.toJson(value)
//    }
//
//    @TypeConverter
//    fun fromString(value: String?): List<String>? {
//        if (value == null) {
//            return emptyList()
//        }
//        val type = object : TypeToken<List<String>>() {}.type
//        return gson.fromJson<List<String>>(value, type)
//    }
//}