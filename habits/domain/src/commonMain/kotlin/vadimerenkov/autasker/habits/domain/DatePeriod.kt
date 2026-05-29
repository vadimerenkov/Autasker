package vadimerenkov.autasker.habits.domain

import java.time.ZonedDateTime

data class DatePeriod(
	val startingDate: ZonedDateTime,
	val endingDate: ZonedDateTime
)

fun ZonedDateTime.isIn(datePeriod: DatePeriod): Boolean {
	return this.isAfter(datePeriod.startingDate) && this.isBefore(datePeriod.endingDate)
}
