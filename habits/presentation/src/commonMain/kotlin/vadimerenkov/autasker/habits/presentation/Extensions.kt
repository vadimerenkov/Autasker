package vadimerenkov.autasker.habits.presentation

import androidx.compose.runtime.Composable
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.custom
import autasker.core.presentation.generated.resources.minutes
import autasker.core.presentation.generated.resources.times_per
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.habits.HabitType
import java.time.YearMonth
import java.time.ZonedDateTime

fun ZonedDateTime.toYearMonth(): YearMonth {
	return YearMonth.of(this.year, this.month)
}

@Composable
fun HabitType.toLocalizedString(): String {
	return when (this) {
		HabitType.SINGLE -> stringResource(Res.string.times_per)
		HabitType.TIME -> stringResource(Res.string.minutes)
		HabitType.CUSTOM -> stringResource(Res.string.custom)
	}
}