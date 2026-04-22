package vadimerenkov.autasker.core.domain

data class TaskCategory(
	val id: Long = 0,
	val title: String? = null,
	val tasks: List<Task> = emptyList(),
	val sorting: Sorting = Sorting.MANUAL,
	val isDefault: Boolean = false,
	val isEditable: Boolean = true,
	val isDeleted: Boolean = false,
	val index: Int,
	val completedOpen: Boolean = true,
	val pageId: Long = 1,
) {
	val canDelete: Boolean
		get() = !isDefault && isEditable
}