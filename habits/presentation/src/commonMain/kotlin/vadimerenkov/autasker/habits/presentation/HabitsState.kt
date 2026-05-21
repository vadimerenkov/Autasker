package vadimerenkov.autasker.habits.presentation

import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import java.time.LocalDate

data class HabitsState(
	val habits: List<Habit> = emptyList(),
	val completions: List<HabitCompletion> = emptyList(),
	val selectedHabit: Habit? = null,
	val isDeleteDialogOpen: Boolean = false,
	val deletingHabit: Habit? = null,
	val openedCalendarDay: LocalDate? = null
)
