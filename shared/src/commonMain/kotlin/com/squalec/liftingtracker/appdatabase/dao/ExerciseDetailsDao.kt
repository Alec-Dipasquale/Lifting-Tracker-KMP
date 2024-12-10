import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.squalec.liftingtracker.appdatabase.models.ExerciseDetails

@Dao
interface ExerciseDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<ExerciseDetails>)

    @Query("SELECT * FROM exercise_details WHERE id = :id")
    suspend fun getExerciseDetailsById(id: String): ExerciseDetails?

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercises(vararg exercises: ExerciseDetails): List<Long>

    @Query("SELECT * FROM exercise_details WHERE LOWER(name) LIKE '%' || LOWER(:search) || '%'")
    suspend fun searchExercises(search: String): List<ExerciseDetails>

    @Query("""
    SELECT DISTINCT * FROM exercise_details
    WHERE LOWER(name) LIKE '%' || LOWER(:search) || '%'
    AND (
        (:muscle1 IS NULL OR (primaryMuscles LIKE '%' || '["' || :muscle1 || '"]%' OR primaryMuscles LIKE '%' || ',"' || :muscle1 || '"%'
                              OR secondaryMuscles LIKE '%' || '["' || :muscle1 || '"]%' OR secondaryMuscles LIKE '%' || ',"' || :muscle1 || '"%'))
        OR (:muscle2 OR (primaryMuscles LIKE '%' || '["' || :muscle2 || '"]%' OR primaryMuscles LIKE '%' || ',"' || :muscle2 || '"%'
                              OR secondaryMuscles LIKE '%' || '["' || :muscle2 || '"]%' OR secondaryMuscles LIKE '%' || ',"' || :muscle2 || '"%'))
        OR (:muscle3 OR (primaryMuscles LIKE '%' || '["' || :muscle3 || '"]%' OR primaryMuscles LIKE '%' || ',"' || :muscle3 || '"%'
                              OR secondaryMuscles LIKE '%' || '["' || :muscle3 || '"]%' OR secondaryMuscles LIKE '%' || ',"' || :muscle3 || '"%'))
        OR (:muscle4 OR (primaryMuscles LIKE '%' || '["' || :muscle4 || '"]%' OR primaryMuscles LIKE '%' || ',"' || :muscle4 || '"%'
                              OR secondaryMuscles LIKE '%' || '["' || :muscle4 || '"]%' OR secondaryMuscles LIKE '%' || ',"' || :muscle4 || '"%'))
        OR (:muscle5 OR (primaryMuscles LIKE '%' || '["' || :muscle5 || '"]%' OR primaryMuscles LIKE '%' || ',"' || :muscle5 || '"%'
                              OR secondaryMuscles LIKE '%' || '["' || :muscle5 || '"]%' OR secondaryMuscles LIKE '%' || ',"' || :muscle5 || '"%'))
        OR (:muscle6 OR (primaryMuscles LIKE '%' || '["' || :muscle6 || '"]%' OR primaryMuscles LIKE '%' || ',"' || :muscle6 || '"%'
                              OR secondaryMuscles LIKE '%' || '["' || :muscle6 || '"]%' OR secondaryMuscles LIKE '%' || ',"' || :muscle6 || '"%'))
        OR (:muscle7 OR (primaryMuscles LIKE '%' || '["' || :muscle7 || '"]%' OR primaryMuscles LIKE '%' || ',"' || :muscle7 || '"%'
                              OR secondaryMuscles LIKE '%' || '["' || :muscle7 || '"]%' OR secondaryMuscles LIKE '%' || ',"' || :muscle7 || '"%'))
    )
""")
    suspend fun searchWithAnyMuscle(
        search: String? = null,
        muscle1: String? = null,
        muscle2: String? = null,
        muscle3: String? = null,
        muscle4: String? = null,
        muscle5: String? = null,
        muscle6: String? = null,
        muscle7: String? = null,
    ): List<ExerciseDetails>








    @Query("""
    SELECT * FROM exercise_details 
    WHERE LOWER(name) LIKE '%' || LOWER(:search) || '%'
    AND (:muscle1 IS NULL OR 
        (LOWER(primaryMuscles) LIKE '%' || LOWER(:muscle1) || '%' 
        OR LOWER(secondaryMuscles) LIKE '%' || LOWER(:muscle1) || '%'))
    AND (:muscle2 IS NULL OR 
        (LOWER(primaryMuscles) LIKE '%' || LOWER(:muscle2) || '%' 
        OR LOWER(secondaryMuscles) LIKE '%' || LOWER(:muscle2) || '%'))
    AND (:muscle3 IS NULL OR 
        (LOWER(primaryMuscles) LIKE '%' || LOWER(:muscle3) || '%' 
        OR LOWER(secondaryMuscles) LIKE '%' || LOWER(:muscle3) || '%'))
    AND (:muscle4 IS NULL OR 
        (LOWER(primaryMuscles) LIKE '%' || LOWER(:muscle4) || '%' 
        OR LOWER(secondaryMuscles) LIKE '%' || LOWER(:muscle4) || '%'))
    AND (:muscle5 IS NULL OR 
        (LOWER(primaryMuscles) LIKE '%' || LOWER(:muscle5) || '%' 
        OR LOWER(secondaryMuscles) LIKE '%' || LOWER(:muscle5) || '%'))
    AND (:muscle6 IS NULL OR 
        (LOWER(primaryMuscles) LIKE '%' || LOWER(:muscle6) || '%' 
        OR LOWER(secondaryMuscles) LIKE '%' || LOWER(:muscle6) || '%'))
    AND (:muscle7 IS NULL OR 
        (LOWER(primaryMuscles) LIKE '%' || LOWER(:muscle7) || '%' 
        OR LOWER(secondaryMuscles) LIKE '%' || LOWER(:muscle7) || '%'))
    AND (:equipment IS NULL OR equipment LIKE '%' || :equipment || '%')
    AND (:level IS NULL OR level LIKE '%' || :level || '%')
    AND (:force IS NULL OR force LIKE '%' || :force || '%')
    AND (:mechanic IS NULL OR mechanic LIKE '%' || :mechanic || '%')
    AND (:category IS NULL OR category LIKE '%' || :category || '%')
""")
    suspend fun searchWithAllMuscles(
        search: String? = null,
        muscle1: String? = null,
        muscle2: String? = null,
        muscle3: String? = null,
        muscle4: String? = null,
        muscle5: String? = null,
        muscle6: String? = null,
        muscle7: String? = null,
        equipment: String? = null,
        level: String? = null,
        force: String? = null,
        mechanic: String? = null,
        category: String? = null
    ): List<ExerciseDetails>


    @Query("SELECT DISTINCT primaryMuscles FROM exercise_details")
    suspend fun getMuscleNames(): List<String>

}