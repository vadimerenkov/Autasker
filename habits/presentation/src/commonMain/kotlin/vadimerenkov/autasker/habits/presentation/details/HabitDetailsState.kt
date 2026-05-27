package vadimerenkov.autasker.habits.presentation.details

import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import vadimerenkov.autasker.habits.domain.DatePeriod
import java.time.LocalDate

data class HabitDetailsState(
	val habit: Habit = Habit(),
	val completions: List<HabitCompletion> = emptyList(),
	val openedCalendarDay: LocalDate? = null,
	val dates: List<DatePeriod> = emptyList(),
	val currentStreak: Int = 0
)
