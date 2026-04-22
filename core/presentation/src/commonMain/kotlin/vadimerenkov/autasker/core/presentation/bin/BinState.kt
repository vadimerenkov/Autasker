package vadimerenkov.autasker.core.presentation.bin

import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.TaskCategory


data class BinState(
	val categories: List<TaskCategory> = emptyList(),
	val tasks: List<Task> = emptyList()
)