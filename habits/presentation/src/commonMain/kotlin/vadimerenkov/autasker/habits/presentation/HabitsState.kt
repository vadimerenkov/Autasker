package vadimerenkov.autasker.habits.presentation

import vadimerenkov.autasker.core.domain.habits.Habit

data class HabitsState(
	val selectedHabit: Habit? = null,
	val habits: List<Habit> = emptyList(),
	val isDeleteDialogOpen: Boolean = false,
	val deletingHabit: Habit? = null,
)
