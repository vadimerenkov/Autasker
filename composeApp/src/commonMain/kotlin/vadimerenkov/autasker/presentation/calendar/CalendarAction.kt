package vadimerenkov.autasker.presentation.calendar

sealed interface CalendarAction {
	data class OnTaskClick(val id: Long): CalendarAction
}