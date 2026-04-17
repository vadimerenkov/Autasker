package vadimerenkov.autasker.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

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