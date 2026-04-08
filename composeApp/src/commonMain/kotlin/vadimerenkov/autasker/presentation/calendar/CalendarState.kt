package vadimerenkov.autasker.presentation.calendar

import vadimerenkov.autasker.domain.Task
import java.time.DayOfWeek

data class CalendarState(
	val tasks: List<Task> = listOf(),
	val firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY
)
