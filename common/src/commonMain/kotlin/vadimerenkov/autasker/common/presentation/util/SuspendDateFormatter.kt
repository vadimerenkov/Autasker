package vadimerenkov.autasker.common.presentation.util

import autasker.common.generated.resources.Res
import autasker.common.generated.resources.period_ago
import autasker.common.generated.resources.period_in
import autasker.common.generated.resources.today
import autasker.common.generated.resources.tomorrow
import autasker.common.generated.resources.yesterday
import org.jetbrains.compose.resources.getString
import vadimerenkov.autasker.common.domain.Period
import vadimerenkov.autasker.common.domain.Time
import vadimerenkov.autasker.common.domain.getLocalizedString
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.time.toKotlinDuration

object SuspendDateFormatter {

	suspend fun formatDuration(date: ZonedDateTime, isAllDay: Boolean): String {
		val in_string = getString(Res.string.period_in)
		val ago_string = getString(Res.string.period_ago)
		val time = if (isAllDay) Time.now() else ZonedDateTime.now()
		val duration = Duration.between(time, date).toKotlinDuration()

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
			duration.inWholeHours.absoluteValue >= 48 -> {
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

		return if (isAllDay && (period == Period.MINUTE || period == Period.HOUR)) {
			val today = getString(Res.string.today)
			val tomorrow = getString(Res.string.tomorrow)
			val yesterday = getString(Res.string.yesterday)

			when {
				date.toLocalDate().equals(Time.today()) -> today
				date.toLocalDate().equals(Time.tomorrow()) -> tomorrow
				date.toLocalDate().equals(Time.yesterday()) -> yesterday
				else -> {
					if (value >= 0) {
						"$in_string $value ${period.getLocalizedString(value)}"
					} else {
						"${value.absoluteValue} ${period.getLocalizedString(value.absoluteValue)} $ago_string"
					}
				}
			}
		} else {
			if (value >= 0) {
				"$in_string $value ${period.getLocalizedString(value)}"
			} else {
				"${value.absoluteValue} ${period.getLocalizedString(value.absoluteValue)} $ago_string"
			}
		}
	}
}