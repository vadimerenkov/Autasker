package vadimerenkov.autasker.presentation.calendar

import vadimerenkov.autasker.domain.Task

data class CalendarState(
	val tasks: List<Task> = listOf()
)
