package com.squalec.liftingtracker.appdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "exercise_details")
data class ExerciseDetails(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "force") val force: String? = null,
    @ColumnInfo(name = "level") val level: String,
    @ColumnInfo(name = "mechanic") val mechanic: String? = null,
    @ColumnInfo(name = "equipment") val equipment: String? = null,
    @ColumnInfo(name = "primaryMuscles") val primaryMuscles: List<String>? = emptyList(),
    @ColumnInfo(name = "secondaryMuscles") val secondaryMuscles: List<String>? = emptyList(),
    @ColumnInfo(name = "instructions") val instructions: List<String>? = emptyList(),
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "images") val images: List<String>? = emptyList(),
){
//    fun getImageBitmap(context: Context): List<Bitmap>? {
//        val assetManager = context.assets
//        var istr: InputStream
//        var bitmap: List<Bitmap> = emptyList()
//
//        try {
//            for (image in images?: emptyList()) {
//                istr = assetManager.open(image)
//                bitmap = bitmap + BitmapFactory.decodeStream(istr)
//            }
//        } catch (e: IOException) {
//            // handle the exception, possibly return null or a default image
//            e.printStackTrace()
//            return null
//        }
//
//        return bitmap
//    }
}