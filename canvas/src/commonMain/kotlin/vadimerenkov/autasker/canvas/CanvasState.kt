package vadimerenkov.autasker.canvas

import vadimerenkov.autasker.core.domain.Task

data class CanvasState(
	val tasks: List<Task> = emptyList()
)
