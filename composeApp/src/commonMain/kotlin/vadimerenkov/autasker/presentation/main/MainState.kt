package vadimerenkov.autasker.presentation.main

import vadimerenkov.autasker.domain.Page
import vadimerenkov.autasker.domain.Sorting
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.domain.TaskCategory

data class MainState(
	val pages: List<Page> = emptyList(),
	val selectedTabIndex: Int = 0,
	val categories: List<TaskCategory> = emptyList(),
	val todayTasks: List<Task> = emptyList(),
	val tomorrowTasks: List<Task> = emptyList(),
	val remainingTasks: List<Task> = emptyList(),
	val todayColumnSorting: Sorting = Sorting.BY_DATE_ASCENDING,
	val tomorrowColumnSorting: Sorting = Sorting.BY_DATE_ASCENDING,
	val todayShowCompleted: Boolean = true,
	val tomorrowShowCompleted: Boolean = true,
) {
	val allTasks: List<Task>
		get() = todayTasks + tomorrowTasks + remainingTasks
}
