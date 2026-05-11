package vadimerenkov.autasker.habits.presentation

import androidx.compose.runtime.Composable
import vadimerenkov.autasker.core.domain.habits.HabitType
import java.time.YearMonth
import java.time.ZonedDateTime

fun ZonedDateTime.toYearMonth(): YearMonth {
	return YearMonth.of(this.year, this.month)
}

@Composable
fun HabitType.toLocalizedString(): String {
	return when (this) {
		HabitType.SINGLE -> "Times"
		HabitType.TIME -> "Minutes"
		HabitType.CUSTOM -> "Custom"
	}
}