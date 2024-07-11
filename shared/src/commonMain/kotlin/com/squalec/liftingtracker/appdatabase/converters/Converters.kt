import androidx.room.TypeConverter
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.models.enums.MuscleGroupType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromExerciseDetailsToJson(value: ExerciseDetails): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun fromJsonToExerciseDetails(value: String): ExerciseDetails {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromExerciseDetailsListToJson(value: List<ExerciseDetails>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun fromJsonToExerciseDetailsList(value: String): List<ExerciseDetails> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromStringListToString(value: List<String>?): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return if (value.isNullOrEmpty()) emptyList() else json.decodeFromString(value)
    }
}
