package vadimerenkov.autasker.habits.presentation

import java.time.YearMonth
import java.time.ZonedDateTime

fun ZonedDateTime.toYearMonth(): YearMonth {
	return YearMonth.of(this.year, this.month)
}