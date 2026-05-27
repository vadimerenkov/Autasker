package vadimerenkov.autasker.habits.domain

import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.Time
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion

class HabitTracker {

	fun calculateDatePeriods(
		habit: Habit,
		completions: List<HabitCompletion>
	): List<DatePeriod> {
		if (completions.isEmpty()) return listOf()

		val dates = mutableListOf<DatePeriod>()
		var endingDate = Time.now()
		var startingDate = when (habit.period) {
			Period.MINUTE,
			Period.HOUR -> throw IllegalStateException()
			Period.DAY -> endingDate.minusDays(1)
			Period.WEEK -> endingDate.minusWeeks(1)
			Period.MONTH -> endingDate.minusMonths(1)
			Period.YEAR -> endingDate.minusYears(1)
		}

		val lastDate = DatePeriod(startingDate = startingDate, endingDate = endingDate)
		dates.add(lastDate)

		while (completions.first().date.isBefore(startingDate)) {
			endingDate = startingDate
			startingDate = when (habit.period) {
				Period.MINUTE,
				Period.HOUR -> throw IllegalStateException()
				Period.DAY -> endingDate.minusDays(1)
				Period.WEEK -> endingDate.minusWeeks(1)
				Period.MONTH -> endingDate.minusMonths(1)
				Period.YEAR -> endingDate.minusYears(1)
			}
			val lastDate = DatePeriod(startingDate = startingDate, endingDate = endingDate)
			dates.add(lastDate)
		}

		return dates.reversed()
	}

	fun calculateCurrentStreak(
		habit: Habit,
		completions: List<HabitCompletion>,
		dates: List<DatePeriod>
	): Int {
		var streak = 0

		for (period in dates.reversed()) {
			val datedCompletions = completions.filter { it.date.isIn(period) }
			println("Dated completions size is ${datedCompletions.size}")
			if (datedCompletions.sumOf { it.quantity } >= habit.times) {
				streak += datedCompletions.sumOf { it.quantity }
				println("sum of datedcompletions is ${datedCompletions.sumOf { it.quantity }}")
			} else {
				println("Breaking")
				break
			}
		}

		return streak
	}
}