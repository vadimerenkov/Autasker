package vadimerenkov.autasker.fakes

import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import vadimerenkov.autasker.core.domain.habits.HabitsRepository

class HabitsRepositoryFake: HabitsRepository {

	override fun getAllHabits(): Flow<List<Habit>> {
		TODO("Not yet implemented")
	}

	override suspend fun getHabit(id: Long): Habit {
		TODO("Not yet implemented")
	}

	override fun getAllCompletions(): Flow<List<HabitCompletion>> {
		TODO("Not yet implemented")
	}

	override fun getCompletionsForHabit(id: Long): Flow<List<HabitCompletion>> {
		TODO("Not yet implemented")
	}

	override suspend fun saveHabit(habit: Habit): Long {
		TODO("Not yet implemented")
	}

	override suspend fun deleteHabit(id: Long) {
		TODO("Not yet implemented")
	}

	override suspend fun saveCompletion(completion: HabitCompletion) {
		TODO("Not yet implemented")
	}

	override suspend fun deleteCompletion(id: Long) {
		TODO("Not yet implemented")
	}

	override suspend fun deleteCompletionsForHabit(id: Long) {
		TODO("Not yet implemented")
	}
}