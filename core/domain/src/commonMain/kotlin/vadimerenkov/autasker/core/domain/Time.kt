package vadimerenkov.autasker.core.domain

import vadimerenkov.autasker.core.domain.settings.Settings
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object Time {

	private val settings: Settings = get().get()

	/**
	Returns real datetime if it is before midnight or after set end of day time; returns the day before otherwise.
	 */
	fun now(): ZonedDateTime {
		val realNow = LocalTime.now()
		val midnight = LocalTime.MIDNIGHT
		val setTime = settings.state.endOfDayTime

		return if (realNow.isAfter(midnight) && realNow.isBefore(setTime)) {
			ZonedDateTime.now().minusDays(1)
		} else {
			ZonedDateTime.now()
		}
	}

	fun today(): LocalDate = now().toLocalDate()
	fun tomorrow(): LocalDate = today().plusDays(1)
	fun yesterday(): LocalDate = today().minusDays(1)

	fun startDayTime(): LocalTime = settings.state.endOfDayTime

	fun todayStart(): ZonedDateTime = today().atTime(startDayTime()).atZone(ZoneId.systemDefault())
	fun todayEnd(): ZonedDateTime = today().plusDays(1).atTime(startDayTime()).atZone(ZoneId.systemDefault()).minusMinutes(1)

	fun tomorrowStart(): ZonedDateTime = tomorrow().atTime(startDayTime()).atZone(ZoneId.systemDefault())
	fun tomorrowEnd(): ZonedDateTime = tomorrow().plusDays(1).atTime(startDayTime()).atZone(ZoneId.systemDefault()).minusMinutes(1)
}