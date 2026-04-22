package vadimerenkov.autasker.core.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.settings.AUTODELETE_COMPLETED
import vadimerenkov.autasker.core.domain.settings.AUTODELETE_FROM_TRASH
import vadimerenkov.autasker.core.domain.settings.AUTOLAUNCH
import vadimerenkov.autasker.core.domain.settings.AutoLaunch
import vadimerenkov.autasker.core.domain.settings.DATE_FORMAT
import vadimerenkov.autasker.core.domain.settings.DELETE_COMPLETED_DAYS
import vadimerenkov.autasker.core.domain.settings.DELETE_FROM_TRASH_DAYS
import vadimerenkov.autasker.core.domain.settings.END_OF_DAY_TIME
import vadimerenkov.autasker.core.domain.settings.FIRST_DAY_OF_WEEK
import vadimerenkov.autasker.core.domain.settings.LANGUAGE
import vadimerenkov.autasker.core.domain.settings.PLAY_SOUND
import vadimerenkov.autasker.core.domain.settings.Settings
import vadimerenkov.autasker.core.domain.settings.THEME
import vadimerenkov.autasker.core.domain.settings.THEME_COLOR
import vadimerenkov.autasker.core.domain.settings.TIME_FORMAT
import vadimerenkov.autasker.core.domain.settings.TRAY

class SettingsViewModel(
	val settings: Settings
): ViewModel() {

	fun onAction(action: SettingsAction) {
		viewModelScope.launch {
			when (action) {
				is SettingsAction.EndOfDayTimeChange -> {
					settings.saveSetting(END_OF_DAY_TIME, action.time.toSecondOfDay())
				}

				is SettingsAction.DeleteFromTrashDaysChange -> {
					settings.saveSetting(DELETE_FROM_TRASH_DAYS, action.days)
				}
				is SettingsAction.ShouldAutoDeleteFromTrash -> {
					settings.saveSetting(AUTODELETE_FROM_TRASH, action.should)
				}

				is SettingsAction.FirstDayOfWeekChange -> {
					settings.saveSetting(FIRST_DAY_OF_WEEK, action.day.value)
				}
				is SettingsAction.LanguageChange -> {
					settings.saveSetting(LANGUAGE, action.language.name)
				}
				is SettingsAction.ThemeChange -> {
					settings.saveSetting(THEME, action.theme.name)
				}

				is SettingsAction.CloseToTray -> {
					settings.saveSetting(TRAY, action.toTray)
				}

				is SettingsAction.DeleteCompletedDaysChange -> {
					settings.saveSetting(DELETE_COMPLETED_DAYS, action.days)
				}
				is SettingsAction.ShouldAutoDeleteCompleted -> {
					settings.saveSetting(AUTODELETE_COMPLETED, action.should)
				}

				is SettingsAction.DateFormatChange -> {
					settings.saveSetting(DATE_FORMAT, action.format.name)
				}
				is SettingsAction.TimeFormatChange -> {
					settings.saveSetting(TIME_FORMAT, action.format.name)
				}

				is SettingsAction.AutoLaunch -> {
					settings.saveSetting(AUTOLAUNCH, action.launch)
					if (action.launch) {
						AutoLaunch.enable()
					} else {
						AutoLaunch.disable()
					}
				}
				is SettingsAction.PlaySoundChange -> {
					settings.saveSetting(PLAY_SOUND, action.play)
				}
				is SettingsAction.ThemeColorChange -> {
					settings.saveSetting(THEME_COLOR, action.color.name)
				}
			}
		}
	}
}