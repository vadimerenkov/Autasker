package vadimerenkov.autasker.settings

import vadimerenkov.autasker.settings.enums.DateFormat
import vadimerenkov.autasker.settings.enums.Language
import vadimerenkov.autasker.settings.enums.Theme
import vadimerenkov.autasker.settings.enums.TimeFormat
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.WeekFields
import java.util.Locale

data class SettingsState(
	val endOfDayTime: LocalTime = LocalTime.MIDNIGHT,
	val autoDeleteFromTrash: Boolean = true,
	val autoDeleteCompleted: Boolean = true,
	val daysUntilDeleteFromTrash: Long = 30,
	val daysUntilDeleteCompleted: Long = 7,
	val firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek,
	val closeToTray: Boolean = true,
	val autoLaunch: Boolean = false,
	val language: Language = Language.ENGLISH,
	val theme: Theme = Theme.DEVICE,
	val timeFormat: TimeFormat = TimeFormat.CLOCK_24,
	val dateFormat: DateFormat = DateFormat.DDMMYYYY
) {
	val deletedCutoffDate: ZonedDateTime
		get() = ZonedDateTime.now().minusDays(daysUntilDeleteFromTrash)

	val completedCutoffDate: ZonedDateTime
		get() = ZonedDateTime.now().minusDays(daysUntilDeleteCompleted)
}
