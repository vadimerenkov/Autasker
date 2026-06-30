package vadimerenkov.autasker.canvas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vadimerenkov.autasker.core.domain.TasksRepository
import vadimerenkov.autasker.core.presentation.main.MainAction
import vadimerenkov.autasker.core.presentation.main.MainViewModel

class CanvasViewModel(
	private val tasksRepository: TasksRepository,
	private val mainViewModel: MainViewModel
): ViewModel() {

	var state by mutableStateOf(CanvasState())

	init {
		tasksRepository
			.getAllTasks()
			.onEach { tasks ->
				state = state.copy(tasks = tasks)
			}.launchIn(viewModelScope)
	}

	fun onAction(action: MainAction) {
		mainViewModel.onAction(action)
	}
}