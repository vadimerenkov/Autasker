package vadimerenkov.autasker.common.domain

data class Subtask(
	val id: Long = 0,
	val parentTaskId: Long,
	val title: String,
	val isCompleted: Boolean = false,
	val index: Int
)
