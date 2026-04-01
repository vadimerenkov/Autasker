package vadimerenkov.autasker.presentation.bin

import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.domain.TaskCategory

data class BinState(
	val categories: List<TaskCategory> = emptyList(),
	val tasks: List<Task> = emptyList()
)