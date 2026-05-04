package vadimerenkov.autasker.core.domain.habits

import vadimerenkov.autasker.core.domain.Period

data class Habit(
	val id: Long = 0,
	val title: String = "",
	val period: Period = Period.DAY,
	val times: Int = 1,
	val type: HabitType = HabitType.SINGLE,
	val customQuantifier: String? = null,
	val completions: List<HabitCompletion> = emptyList()
)