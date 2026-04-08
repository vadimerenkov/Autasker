package vadimerenkov.autasker.presentation.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vadimerenkov.autasker.domain.TasksRepository

class CalendarViewModel(
	repository: TasksRepository
): ViewModel() {

	var state by mutableStateOf(CalendarState())
		private set

	init {
		repository
			.getAllTasks()
			.onEach { list ->
				state = state.copy(tasks = list)
			}.launchIn(viewModelScope)
	}
}