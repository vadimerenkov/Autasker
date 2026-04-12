package vadimerenkov.autasker.presentation.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vadimerenkov.autasker.domain.TasksRepository
import vadimerenkov.autasker.settings.Settings

class CalendarViewModel(
	repository: TasksRepository,
	settings: Settings
): ViewModel() {

	var state by mutableStateOf(CalendarState())
		private set

	init {
		repository
			.getAllTasks()
			.onEach { list ->
				state = state.copy(tasks = list)
			}.launchIn(viewModelScope)

		snapshotFlow { settings.state }
			.onEach { settingsState ->
				state = state.copy(firstDayOfWeek = settingsState.firstDayOfWeek, startDayHour = settingsState.endOfDayTime.hour)
			}.launchIn(viewModelScope)
	}

	fun onAction(action: CalendarAction) {
		when (action) {
			CalendarAction.DismissDialog -> {
				state = state.copy(isDayDialogOpen = false, selectedDay = null)
			}
			is CalendarAction.OnDaySelected -> {
				state = state.copy(isDayDialogOpen = true, selectedDay = action.day)
			}
			else -> Unit
		}
	}
}