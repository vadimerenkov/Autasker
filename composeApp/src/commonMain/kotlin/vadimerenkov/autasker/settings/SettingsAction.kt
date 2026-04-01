package vadimerenkov.autasker.settings

import vadimerenkov.autasker.settings.enums.DateFormat
import vadimerenkov.autasker.settings.enums.Language
import vadimerenkov.autasker.settings.enums.Theme
import vadimerenkov.autasker.settings.enums.TimeFormat
import java.time.DayOfWeek
import java.time.LocalTime

sealed interface SettingsAction {

	data class EndOfDayTimeChange(val time: LocalTime): SettingsAction
	data class DeleteFromTrashDaysChange(val days: Long): SettingsAction
	data class DeleteCompletedDaysChange(val days: Long): SettingsAction
	data class ShouldAutoDeleteFromTrash(val should: Boolean): SettingsAction
	data class ShouldAutoDeleteCompleted(val should: Boolean): SettingsAction
	data class FirstDayOfWeekChange(val day: DayOfWeek): SettingsAction
	data class LanguageChange(val language: Language): SettingsAction
	data class ThemeChange(val theme: Theme): SettingsAction
	data class TimeFormatChange(val format: TimeFormat): SettingsAction
	data class DateFormatChange(val format: DateFormat): SettingsAction
	data class CloseToTray(val toTray: Boolean): SettingsAction
	data class AutoLaunch(val launch: Boolean): SettingsAction
}