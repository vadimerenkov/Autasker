package vadimerenkov.autasker.core.presentation.task_edit.datetime

import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.RepeatMode
import vadimerenkov.autasker.core.domain.RepeatState
import vadimerenkov.autasker.core.domain.reminders.Reminder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

data class DateTimeState(
	val isOpen: Boolean = false,
	val date: LocalDate = LocalDate.now(),
	val time: LocalTime = LocalTime.now(),
	val isRepeated: Boolean = false,
	val repeatMode: RepeatMode = RepeatMode.ON_EXACT,
	val times: Long? = 1L,
	val period: Period? = Period.DAY,
	val hasDate: Boolean = true,
	val hasTime: Boolean = false,
	val weekdays: List<DayOfWeek> = emptyList(),
	val reminders: List<Reminder> = listOf()
) {
	val dateTime: ZonedDateTime
		get() = date.atTime(time).atZone(ZoneId.systemDefault())

	val repeatState: RepeatState
		get() = RepeatState(
			isRepeating = isRepeated,
			mode = repeatMode,
			times = times ?: 1,
			period = period ?: Period.DAY,
			weekDays = weekdays
		)

	val isTimesValueValid: Boolean
		get() = times != null

	val isPeriodValueValid: Boolean
		get() = period != null

	val isWeekdaysValid: Boolean
		get() = if (period == Period.WEEK) weekdays.isNotEmpty() else true

	val isValid: Boolean
		get() = isTimesValueValid && isPeriodValueValid && isWeekdaysValid
}