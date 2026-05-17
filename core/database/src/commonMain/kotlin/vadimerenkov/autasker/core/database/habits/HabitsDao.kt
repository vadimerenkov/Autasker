package vadimerenkov.autasker.core.database.habits

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitsDao {

	@Query("SELECT * FROM habitdata")
	fun getAllHabits(): Flow<List<HabitData>>

	@Query("SELECT * FROM habitdata WHERE id = :id")
	suspend fun getHabit(id: Long): HabitData

	@Query("SELECT * FROM habitcompletiondata WHERE habitId = :habitId")
	fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletionData>>

	@Query("SELECT * FROM habitcompletiondata")
	fun getAllCompletions(): Flow<List<HabitCompletionData>>

	@Upsert
	suspend fun saveHabit(habit: HabitData): Long

	@Query("DELETE FROM habitdata WHERE id = :id")
	suspend fun deleteHabit(id: Long)

	@Upsert
	suspend fun saveCompletion(completion: HabitCompletionData)

	@Query("DELETE FROM habitcompletiondata WHERE id = :id")
	suspend fun deleteCompletion(id: Long)

	@Query("DELETE FROM habitcompletiondata WHERE habitId = :id")
	suspend fun deleteCompletionsForHabit(id: Long)
}

