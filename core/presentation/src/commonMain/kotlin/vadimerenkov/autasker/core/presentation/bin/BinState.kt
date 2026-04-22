package vadimerenkov.autasker.core.presentation.bin

import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.domain.TaskCategory

data class BinState(
	val categories: List<TaskCategory> = emptyList(),
	val tasks: List<Task> = emptyList()
)