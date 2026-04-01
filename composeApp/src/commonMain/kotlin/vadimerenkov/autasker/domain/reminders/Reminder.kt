package vadimerenkov.autasker.domain.reminders

import vadimerenkov.autasker.domain.Period
import java.time.LocalTime

data class Reminder(
	val id: Long = 0,
	val parentTaskId: Long = 0,
	val period: Period = Period.HOUR,
	val times: Long = 0,
	val time: LocalTime = LocalTime.NOON
)