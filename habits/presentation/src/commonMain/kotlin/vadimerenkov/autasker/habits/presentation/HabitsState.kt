package vadimerenkov.autasker.habits.presentation

import vadimerenkov.autasker.core.domain.habits.Habit

data class HabitsState(
	val habits: List<Habit> = emptyList()
)
