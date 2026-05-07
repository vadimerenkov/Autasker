package vadimerenkov.autasker.habits.presentation

import java.time.LocalDate

sealed interface HabitsAction {
	data object NewHabitClick: HabitsAction
	data class OnHabitClick(val id: Long): HabitsAction
	data class OnHabitTitleChange(val id: Long, val title: String): HabitsAction
	data class OnCalendarDayClick(val date: LocalDate, val id: Long): HabitsAction
	data class OnCalendarDayUnclick(val completionId: Long): HabitsAction
}