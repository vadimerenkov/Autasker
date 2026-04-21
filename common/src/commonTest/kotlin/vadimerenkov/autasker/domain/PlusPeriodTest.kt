package vadimerenkov.autasker.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import vadimerenkov.autasker.common.domain.Period
import vadimerenkov.autasker.common.domain.RepeatMode
import vadimerenkov.autasker.common.domain.RepeatState
import vadimerenkov.autasker.common.domain.plusPeriod
import vadimerenkov.autasker.common.domain.roundToMinutes
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime

class PlusPeriodTest {

	@Test
	fun `Adding weekly repeat period yields next tuesday`() {
		val repeatState = RepeatState(
			isRepeating = true,
			mode = RepeatMode.ON_EXACT,
			period = Period.WEEK,
			times = 3,
			weekDays = listOf(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY)
		)

		val startingDate = ZonedDateTime.of(2025, 3, 10, 12, 12, 12, 12, ZoneId.systemDefault())
		val date = startingDate.plusPeriod(repeatState, DayOfWeek.MONDAY)
		assertThat(date.roundToMinutes()).isEqualTo(startingDate.plusDays(1).roundToMinutes())
	}

	@Test
	fun `Adding daily repeat period yields next saturday`() {
		val repeatState = RepeatState(
			isRepeating = true,
			mode = RepeatMode.ON_EXACT,
			period = Period.DAY,
			times = 4
		)
		val startingDate = ZonedDateTime.of(2026, 3, 25, 12, 12, 12, 12, ZoneId.systemDefault())
		val date = startingDate.plusPeriod(repeatState, DayOfWeek.MONDAY)
		assertThat(date.roundToMinutes()).isEqualTo(startingDate.plusDays(4).roundToMinutes())
	}
}