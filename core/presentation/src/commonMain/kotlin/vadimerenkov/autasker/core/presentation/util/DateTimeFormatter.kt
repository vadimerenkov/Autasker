package vadimerenkov.autasker.core.presentation.util

import androidx.compose.runtime.Composable
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.period_ago
import autasker.core.presentation.generated.resources.period_in
import autasker.core.presentation.generated.resources.today
import autasker.core.presentation.generated.resources.tomorrow
import autasker.core.presentation.generated.resources.yesterday
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.GlobalContext.get
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.Time
import vadimerenkov.autasker.core.domain.settings.Settings
import vadimerenkov.autasker.core.domain.settings.enums.DateFormat
import vadimerenkov.autasker.core.domain.settings.enums.TimeFormat
import vadimerenkov.autasker.core.presentation.extensions.toLocalizedString
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.time.toKotlinDuration

object ComposableDateFormatter {
	val settings: Settings = get().get()

	fun formatDate(date: ZonedDateTime, isAllDay: Boolean): String {
		val datePattern = when (settings.state.value.dateFormat) {
			DateFormat.YYYYMMDD -> "yyyy.MM.dd"
			DateFormat.DDMMYYYY -> "dd.MM.yyyy"
			DateFormat.MMDDYYYY -> "MM.dd.yyyy"
		}
		val timePattern = when (settings.state.value.timeFormat) {
			TimeFormat.CLOCK_12 -> "h:mm a"
			TimeFormat.CLOCK_24 -> "HH:mm"
		}
		val pattern = if (isAllDay) datePattern else "$datePattern $timePattern"
		val formatter = DateTimeFormatter.ofPattern(pattern)
		return formatter.format(date)
	}

	fun formatTime(time: LocalTime): String {

		val timePattern = when (settings.state.value.timeFormat) {
			TimeFormat.CLOCK_12 -> "h:mm a"
			TimeFormat.CLOCK_24 -> "HH:mm"
		}
		val formatter = DateTimeFormatter.ofPattern(timePattern)
		return formatter.format(time)
	}

	@Composable
	fun formatDuration(date: ZonedDateTime, isAllDay: Boolean): String {
		val in_string = stringResource(Res.string.period_in)
		val ago_string = stringResource(Res.string.period_ago)
		val time = if (isAllDay) Time.now() else ZonedDateTime.now()
		val duration = java.time.Duration.between(time, date).toKotlinDuration()

		var value: Int

		val period = when {
			duration.inWholeDays.absoluteValue >= 365 -> {
				value = (duration.inWholeDays / 365.0).roundToInt()
				Period.YEAR
			}
			duration.inWholeDays.absoluteValue >= 60 -> {
				value = (duration.inWholeDays / 30.0).roundToInt()
				Period.MONTH
			}
			duration.inWholeHours.absoluteValue >= 24 -> {
				value = (duration.inWholeHours / 24.0).roundToInt()
				Period.DAY
			}
			duration.inWholeHours.absoluteValue >= 1 -> {
				value = (duration.inWholeMinutes / 60.0).roundToInt()
				Period.HOUR
			}
			else -> {
				value = duration.inWholeMinutes.toInt()
				Period.MINUTE
			}
		}

		return if (isAllDay) {
			val today = stringResource(Res.string.today).lowercase()
			val tomorrow = stringResource(Res.string.tomorrow).lowercase()
			val yesterday = stringResource(Res.string.yesterday).lowercase()

			when {
				date.toLocalDate().equals(Time.today()) -> today
				date.toLocalDate().equals(Time.tomorrow()) -> tomorrow
				date.toLocalDate().equals(Time.yesterday()) -> yesterday
				else -> {
					if (value >= 0) {
						"$in_string $value ${period.toLocalizedString(value)}"
					} else {
						"${value.absoluteValue} ${period.toLocalizedString(value.absoluteValue)} $ago_string"
					}
				}
			}
		} else {
			if (value >= 0) {
				"$in_string $value ${period.toLocalizedString(value)}"
			} else {
				"${value.absoluteValue} ${period.toLocalizedString(value.absoluteValue)} $ago_string"
			}
		}
	}
}