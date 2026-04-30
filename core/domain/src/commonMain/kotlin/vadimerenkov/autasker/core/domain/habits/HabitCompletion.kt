package vadimerenkov.autasker.core.domain.habits

import java.time.ZonedDateTime

data class HabitCompletion(
	val id: Long = 0,
	val habitId: Long,
	val date: ZonedDateTime,
	val quantity: Int
)
