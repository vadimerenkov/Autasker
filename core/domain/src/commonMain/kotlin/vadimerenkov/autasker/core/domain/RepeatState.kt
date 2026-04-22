package vadimerenkov.autasker.core.domain

import java.time.DayOfWeek

data class RepeatState(
	val isRepeating: Boolean = false,
	val mode: RepeatMode = RepeatMode.ON_EXACT,
	val period: Period = Period.DAY,
	val times: Long = 1L,
	val weekDays: List<DayOfWeek> = emptyList(),
)

enum class RepeatMode {
	ON_COMPLETION,
	ON_EXACT
	/*
	FORGIVING,
	ALWAYS
	 */
}

