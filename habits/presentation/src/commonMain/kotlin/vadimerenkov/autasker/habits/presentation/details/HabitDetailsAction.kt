package vadimerenkov.autasker.habits.presentation.details

import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import java.time.LocalDate

sealed interface HabitDetailsAction {
	data class OnCalendarDayClick(val date: LocalDate): HabitDetailsAction
	data object DayDialogDismiss: HabitDetailsAction
	data class DayDialogSave(val completions: List<HabitCompletion>): HabitDetailsAction
}