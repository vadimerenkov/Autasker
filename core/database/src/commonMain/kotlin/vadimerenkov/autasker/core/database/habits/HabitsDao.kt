package vadimerenkov.autasker.core.database.habits

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitsDao {

	@Query("SELECT * FROM habitdata")
	fun getAllHabits(): Flow<List<HabitData>>

	@Upsert
	suspend fun saveHabit(habit: HabitData)
}

