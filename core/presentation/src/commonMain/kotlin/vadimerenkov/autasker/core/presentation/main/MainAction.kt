package vadimerenkov.autasker.core.presentation.main

import vadimerenkov.autasker.core.domain.Page
import vadimerenkov.autasker.core.domain.Sorting
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.domain.TaskCategory

sealed interface MainAction {
	data class CheckmarkToggle(
		val id: Long,
		val isChecked: Boolean
	): MainAction
	data class OnTaskClick(val id: Long): MainAction
	data class NewTaskClick(val categoryId: Long): MainAction
	data object NewColumnClick: MainAction
	data class DeleteColumn(val id: Long): MainAction
	data class SetColumnDefault(val id: Long): MainAction
	data class ChangeColumnTitle(val id: Long, val title: String): MainAction
	data class DeleteTask(val id: Long): MainAction
	data class SubtaskToggle(
		val id: Long,
		val index: Int
	): MainAction
	data class MoveTaskClick(val id: Long): MainAction
	data class MoveColumnClick(val id: Long): MainAction
	data class MoveTaskCategoryChosen(
		val taskId: Long,
		val categoryId: Long
	): MainAction
	data class MoveCategoryPageChosen(
		val categoryId: Long,
		val pageId: Long
	): MainAction
	data class SetForToday(val id: Long): MainAction
	data class SetForTomorrow(val id: Long): MainAction
	data class ClearDate(val id: Long): MainAction
	data class DeleteTaskForever(val id: Long): MainAction
	data class RestoreTask(val id: Long): MainAction
	data class ReorderTasks(val tasks: List<Task>): MainAction
	data class ChangeColumnSorting(val id: Long, val sorting: Sorting): MainAction
	data class ChangeColumnCompletedView(val id: Long, val isShowing: Boolean): MainAction
	data class ChangeColumnsIndices(val categories: List<TaskCategory>): MainAction
	data object NewTabClick: MainAction
	data class OnTabClick(val tabIndex: Int): MainAction
	data class DeleteTabClick(val id: Long): MainAction
	data class TabRename(val id: Long, val title: String?): MainAction
	data class SavePages(val list: List<Page>): MainAction
}