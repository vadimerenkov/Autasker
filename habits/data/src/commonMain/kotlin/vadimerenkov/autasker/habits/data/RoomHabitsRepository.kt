package vadimerenkov.autasker.habits.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import vadimerenkov.autasker.core.database.habits.HabitsDao
import vadimerenkov.autasker.core.database.habits.toCompletion
import vadimerenkov.autasker.core.database.habits.toData
import vadimerenkov.autasker.core.database.habits.toHabit
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import vadimerenkov.autasker.habits.domain.HabitsRepository

class RoomHabitsRepository(
	private val dao: HabitsDao
): HabitsRepository {

	override fun getAllHabits(): Flow<List<Habit>> {
		val completions = dao
			.getAllCompletions()
			.map { it.map { it.toCompletion() } }

		return dao
			.getAllHabits()
			.map { it.map { it.toHabit() } }
			.combine(completions) { habits, completions ->
				habits.map { habit ->
					habit.copy(completions = completions.filter { it.habitId == habit.id })
				}
			}
	}

	override suspend fun getHabit(id: Long): Habit {
		return dao.getHabit(id).toHabit()
	}

	override fun getAllCompletions(): Flow<List<HabitCompletion>> {
		return dao.getAllCompletions().map { it.map { it.toCompletion() } }
	}

	override suspend fun saveHabit(habit: Habit): Long {
		return dao.saveHabit(habit.toData())
	}

	override suspend fun deleteHabit(id: Long) {
		dao.deleteHabit(id)
	}

	override suspend fun saveCompletion(completion: HabitCompletion) {
		dao.saveCompletion(completion.toData())
	}

	override suspend fun deleteCompletion(id: Long) {
		dao.deleteCompletion(id)
	}

	override suspend fun deleteCompletionsForHabit(id: Long) {
		dao.deleteCompletionsForHabit(id)
	}

}