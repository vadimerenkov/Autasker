package vadimerenkov.autasker.habits.domain

import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion


interface HabitsRepository {

	fun getAllHabits(): Flow<List<Habit>>
	suspend fun getHabit(id: Long): Habit
	fun getAllCompletions(): Flow<List<HabitCompletion>>
	suspend fun saveHabit(habit: Habit)
	suspend fun deleteHabit(id: Long)
	suspend fun saveCompletion(completion: HabitCompletion)
	suspend fun deleteCompletion(id: Long)

}