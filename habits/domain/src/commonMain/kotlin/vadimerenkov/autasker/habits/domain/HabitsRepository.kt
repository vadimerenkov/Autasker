package vadimerenkov.autasker.habits.domain

import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.core.domain.habits.Habit


interface HabitsRepository {

	fun getAllHabits(): Flow<List<Habit>>
	suspend fun saveHabit(habit: Habit)
}