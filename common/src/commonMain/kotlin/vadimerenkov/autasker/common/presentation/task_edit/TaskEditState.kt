package vadimerenkov.autasker.common.presentation.task_edit

import vadimerenkov.autasker.common.domain.RepeatState
import vadimerenkov.autasker.common.domain.Subtask
import vadimerenkov.autasker.common.domain.TaskCategory
import vadimerenkov.autasker.common.domain.reminders.Reminder
import java.time.ZonedDateTime

data class TaskEditState(
	val isLoading: Boolean = true,
	val id: Long = 0,
	val categoryId: Long = 0,
	val index: Int = 0,
	val title: String = "",
	val description: String? = null,
	val dueDate: ZonedDateTime? = null,
	val repeatState: RepeatState = RepeatState(),
	val isCompleted: Boolean = false,
	val completedDate: ZonedDateTime? = null,
	val isDeleted: Boolean = false,
	val deletedDate: ZonedDateTime? = null,
	val isAllDay: Boolean = true,
	val importance: Int = 0,
	val subtasks: List<Subtask> = emptyList(),
	val reminders: List<Reminder> = emptyList(),

	val tag: String? = null,
	val categories: List<TaskCategory> = emptyList()
) {
	val isValid: Boolean
		get() = title.isNotBlank()
}