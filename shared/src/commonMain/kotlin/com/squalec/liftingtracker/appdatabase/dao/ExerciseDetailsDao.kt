import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails

@Dao
interface ExerciseDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<ExerciseDetails>)

    @Query("SELECT * FROM exercise_details")
    suspend fun getAllExercises(): List<ExerciseDetails>

    @Query("SELECT * FROM exercise_details WHERE category = :category")
    suspend fun getExercisesByCategory(category: String): List<ExerciseDetails>

    @Query("SELECT * FROM exercise_details WHERE equipment = :equipmentType")
    suspend fun getExercisesByEquipment(equipmentType: String): List<ExerciseDetails>

    @Query("SELECT * FROM exercise_details WHERE level = :level")
    suspend fun getExercisesByLevel(level: String): List<ExerciseDetails>

    @Query("SELECT * FROM exercise_details WHERE name = :name")
    suspend fun getExerciseByName(name: String): ExerciseDetails

    @Query("SELECT * FROM exercise_details WHERE force = :force")
    suspend fun getExercisesByForce(force: String): List<ExerciseDetails>

    @Query("SELECT * FROM exercise_details WHERE primaryMuscles LIKE '%' || :muscle || '%'")
    suspend fun getExercisesByMuscle(muscle: String): List<ExerciseDetails>

    @Query("SELECT * FROM exercise_details WHERE secondaryMuscles LIKE '%' || :muscle || '%'")
    suspend fun getExercisesBySecondaryMuscle(muscle: String): List<ExerciseDetails>

    @Query("SELECT * FROM exercise_details WHERE mechanic = :mechanic")
    suspend fun getExercisesByType(mechanic: String): List<ExerciseDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(vararg exercises: ExerciseDetails)

    @Query("SELECT * FROM exercise_details WHERE LOWER(name) LIKE '%' || LOWER(:search) || '%'")
    suspend fun searchExercises(search: String): List<ExerciseDetails>

}