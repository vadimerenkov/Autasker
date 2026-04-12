package vadimerenkov.autasker.presentation.calendar

import vadimerenkov.autasker.domain.Task
import java.time.DayOfWeek
import java.time.LocalDate

data class CalendarState(
	val tasks: List<Task> = listOf(),
	val firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
	val isDayDialogOpen: Boolean = false,
	val selectedDay: LocalDate? = null,
	val startDayHour: Int = 0
)
