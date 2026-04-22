package vadimerenkov.autasker.core.domain

import vadimerenkov.autasker.core.domain.reminders.Reminder
import java.time.DayOfWeek
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

data class Task(
	val id: Long = 0,
	val categoryId: Long = 0,
	val index: Int = 0,
	val title: String = "",
	val description: String? = null,
	val dueDate: ZonedDateTime? = null,
	val repeatState: RepeatState = RepeatState(),
	val isCompleted: Boolean = false,
	val completedDate: ZonedDateTime? = null,
	val isDeleted: Boolean = false,
	val deletedDate: ZonedDateTime? = null,
	val isAllDay: Boolean = true,
	val importance: Int = 0,
	val subtasks: List<Subtask> = emptyList(),
	val reminders: List<Reminder> = emptyList(),
) {
	val timeRemaining: Duration?
		get() {
			return if (dueDate == null) {
				null
			} else {
				val countedDate = if (isAllDay) dueDate.with(Time.startDayTime()) else dueDate
				val duration = java.time.Duration.between(Time.now(),countedDate)
				duration.toKotlinDuration()
			}
		}

	fun calculateNewDate(firstDayOfWeek: DayOfWeek): ZonedDateTime? {

		val startingDate = when (repeatState.mode) {
			RepeatMode.ON_COMPLETION -> completedDate ?: Time.now()
			else -> dueDate
		}

		return startingDate?.plusPeriod(repeatState, firstDayOfWeek)
	}
}
