package vadimerenkov.autasker.presentation.calendar

import java.time.LocalDate

sealed interface CalendarAction {
	data class OnTaskClick(val id: Long): CalendarAction
	data object DismissDialog: CalendarAction
	data class OnDaySelected(val day: LocalDate): CalendarAction
}