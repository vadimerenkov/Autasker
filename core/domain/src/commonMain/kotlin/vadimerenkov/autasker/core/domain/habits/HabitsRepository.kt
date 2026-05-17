package vadimerenkov.autasker.core.domain.habits

import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

	fun getAllHabits(): Flow<List<Habit>>
	suspend fun getHabit(id: Long): Habit
	fun getAllCompletions(): Flow<List<HabitCompletion>>
	suspend fun saveHabit(habit: Habit): Long
	suspend fun deleteHabit(id: Long)
	suspend fun saveCompletion(completion: HabitCompletion)
	suspend fun deleteCompletion(id: Long)
	suspend fun deleteCompletionsForHabit(id: Long)
}