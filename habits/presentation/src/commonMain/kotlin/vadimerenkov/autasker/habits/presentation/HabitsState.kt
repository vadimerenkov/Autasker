package vadimerenkov.autasker.habits.presentation

import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion

data class HabitsState(
	val habits: List<Habit> = emptyList(),
	val completions: List<HabitCompletion> = emptyList(),
	val selectedHabit: Habit? = null
)
