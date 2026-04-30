package vadimerenkov.autasker.core.database.habits

import java.time.Instant

data class HabitCompletionData(
	val id: Long = 0,
	val habitId: Long,
	val date: Instant,
	val quantity: Int
)
