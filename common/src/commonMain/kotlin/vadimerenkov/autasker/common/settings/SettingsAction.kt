package vadimerenkov.autasker.common.settings

import vadimerenkov.autasker.common.presentation.theme.ThemeColor
import vadimerenkov.autasker.common.settings.enums.DateFormat
import vadimerenkov.autasker.common.settings.enums.Language
import vadimerenkov.autasker.common.settings.enums.Theme
import vadimerenkov.autasker.common.settings.enums.TimeFormat
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
	data class PlaySoundChange(val play: Boolean): SettingsAction
	data class ThemeColorChange(val color: ThemeColor): SettingsAction
}