package vadimerenkov.autasker.habits.presentation

import androidx.compose.runtime.Composable
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.custom
import autasker.core.presentation.generated.resources.minutes
import autasker.core.presentation.generated.resources.times_per
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.habits.HabitType
import vadimerenkov.autasker.core.presentation.extensions.toLocalizedString
import java.time.YearMonth
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

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

@Composable
fun calculateTimeString(minutes: Int): String {
	val duration = minutes.minutes
	val t = StringBuilder().apply {
		var remainingDays = duration.inWholeDays
		if (remainingDays > 365 ) {
			val years = (duration.inWholeDays / 365).toInt()
			append("$years ${Period.YEAR.toLocalizedString(years)} ")
			remainingDays-= years*365
		}
		if (remainingDays > 30) {
			val months = (remainingDays / 30).toInt()
			append("$months ${Period.MONTH.toLocalizedString(months)} ")
		}
		if (remainingDays > 0) {
			append("$remainingDays ${Period.DAY.toLocalizedString(remainingDays.toInt())} ")
		}
		val hours = duration.inWholeHours - duration.inWholeDays * 24
		if (hours > 0) {
			append("$hours ${Period.HOUR.toLocalizedString(hours.toInt())} ")
		}
		val minutes = duration.inWholeMinutes - hours * 60 - duration.inWholeDays* 24 * 60
		append("$minutes ${Period.MINUTE.toLocalizedString(minutes.toInt())} ")
	}

	return t.toString()
}