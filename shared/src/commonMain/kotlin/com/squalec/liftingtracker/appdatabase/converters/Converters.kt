import androidx.room.TypeConverter
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails
import com.squalec.liftingtracker.appdatabase.models.UserExercise
import com.squalec.liftingtracker.appdatabase.models.UserSet
import com.squalec.liftingtracker.appdatabase.models.UserWorkoutSession
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

    @TypeConverter
    fun fromUserWorkoutSessionToString(value: List<UserWorkoutSession>?): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToUserWorkoutSession(value: String?): List<UserWorkoutSession>? {
        return if (value.isNullOrEmpty()) emptyList() else json.decodeFromString(value)
    }

    @TypeConverter
    fun fromUserExerciseToString(value: List<UserExercise>?): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToUserExercise(value: String?): List<UserExercise>? {
        return if (value.isNullOrEmpty()) emptyList() else json.decodeFromString(value)
    }

    @TypeConverter
    fun fromUserSetToString(value: List<UserSet>?): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToUserSet(value: String?): List<UserSet>? {
        return if (value.isNullOrEmpty()) emptyList() else json.decodeFromString(value)
    }
}
