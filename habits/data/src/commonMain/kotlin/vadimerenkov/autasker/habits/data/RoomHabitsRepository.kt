package vadimerenkov.autasker.habits.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vadimerenkov.autasker.core.database.habits.HabitsDao
import vadimerenkov.autasker.core.database.habits.toData
import vadimerenkov.autasker.core.database.habits.toHabit
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.habits.domain.HabitsRepository

class RoomHabitsRepository(
	private val dao: HabitsDao
): HabitsRepository {

	override fun getAllHabits(): Flow<List<Habit>> {
		return dao.getAllHabits().map { it.map { it.toHabit() } }
	}

	override suspend fun saveHabit(habit: Habit) {
		dao.saveHabit(habit.toData())
	}

}