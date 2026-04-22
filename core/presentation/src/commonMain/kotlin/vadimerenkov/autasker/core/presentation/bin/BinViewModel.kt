package vadimerenkov.autasker.core.presentation.bin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.TasksRepository
import vadimerenkov.autasker.common.domain.Time
import vadimerenkov.autasker.common.domain.minusReminder
import vadimerenkov.autasker.common.domain.reminders.ReminderService
import vadimerenkov.autasker.common.presentation.main.MainAction

class BinViewModel(
	private val repository: TasksRepository,
	private val reminderService: ReminderService
): ViewModel() {

	var state by mutableStateOf(BinState())

	init {
		repository
			.getDeletedTasks()
			.onEach { list ->
				val grouped = list.groupBy { it.categoryId }
				viewModelScope.launch {
					val categories = grouped.map {
						repository.getCategory(it.key).copy(tasks = it.value)
					}
					state = state.copy(categories = categories, tasks = list)
				}
			}.launchIn(viewModelScope)
	}

	fun onAction(action: BinAction) {
		when (action) {
			BinAction.Clear -> {
				viewModelScope.launch {
					repository.deleteTasks(state.tasks)
					state.categories.forEach { category ->
						if (repository.getTaskCountForCategory(category.id) == 0) {
							repository.deleteCategory(category.id)
						}
					}
				}
			}
			else -> Unit
		}
	}

	fun onTaskAction(action: MainAction) {
		when (action) {
			is MainAction.RestoreTask -> {
				viewModelScope.launch {
					state.tasks.first { it.id == action.id }.let { task ->
						repository.saveTask(task.copy(isDeleted = false))
						rescheduleRemindersForTask(task)
					}
				}
			}
			is MainAction.DeleteTaskForever -> {
				viewModelScope.launch {
					state.tasks.first { it.id == action.id }.let { task ->
						repository.deleteTasks(listOf(task))
					}
				}
			}
			else -> Unit
		}
	}

	private suspend fun rescheduleRemindersForTask(task: Task) {
		reminderService.cancelRemindersForTask(task.id)
		repository
			.getRemindersForTask(task.id)
			.forEach { reminder ->
				if (task.dueDate?.minusReminder(reminder)?.isBefore(Time.now()) == false) {
					reminderService.scheduleReminder(task.id, task.dueDate.minusReminder(reminder))
				}
			}
	}
}