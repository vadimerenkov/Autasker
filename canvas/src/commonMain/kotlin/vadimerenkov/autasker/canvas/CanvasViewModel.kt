package vadimerenkov.autasker.canvas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vadimerenkov.autasker.core.domain.TasksRepository
import kotlin.random.Random

class CanvasViewModel(
	tasksRepository: TasksRepository
): ViewModel() {

	var state by mutableStateOf(CanvasState())

	init {
		tasksRepository
			.getAllTasks()
			.onEach { tasks ->
				state = state.copy(tasks = tasks.map { task ->
					CanvasTaskItem(
						task = task,
						percentageOffset = Offset(Random.nextFloat(), Random.nextFloat())
					)
				})
			}.launchIn(viewModelScope)
	}
}