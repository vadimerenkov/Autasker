package vadimerenkov.autasker.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import vadimerenkov.autasker.domain.Page
import vadimerenkov.autasker.domain.Subtask
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.domain.TaskCategory
import vadimerenkov.autasker.domain.TasksRepository
import vadimerenkov.autasker.domain.Time
import vadimerenkov.autasker.domain.minusReminder
import vadimerenkov.autasker.domain.reminders.Reminder
import vadimerenkov.autasker.domain.reminders.ReminderService
import vadimerenkov.autasker.domain.roundToMinutes
import vadimerenkov.autasker.navigation.EditGraph
import vadimerenkov.autasker.navigation.NewDayRoute
import vadimerenkov.autasker.settings.Settings
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class MainViewModel(
	private val repository: TasksRepository,
	private val reminderService: ReminderService,
	private val audioPlayer: AudioPlayer,
	private val settings: Settings,
	private val backstack: MutableList<NavKey>
): ViewModel() {

	var state by mutableStateOf(MainState())
		private set

	private var changeTitleJob: Job? = null

	init {
		if (settings.state.autoDeleteFromTrash) {
			val tasksToDelete = mutableListOf<Task>()
			val subtasksToDelete = mutableListOf<Subtask>()
			viewModelScope.launch {
				repository.getDeletedTasks()
					.first()
					.forEach { task ->
						val deletedDate = task.deletedDate
						if (deletedDate?.isBefore(settings.state.deletedCutoffDate) == true) {
							tasksToDelete.add(task)
							subtasksToDelete.addAll(task.subtasks)
						}
					}
				println("Deleting overdue tasks: $tasksToDelete")
				launch {
					repository.deleteTasks(tasksToDelete)
				}
				launch {
					repository.deleteSubtasks(subtasksToDelete)
				}
			}
		}

		if (settings.state.autoDeleteCompleted) {
			val tasksToDelete = mutableListOf<Task>()
			viewModelScope.launch {
				repository.getCompletedTasks()
					.forEach { task ->
						if (task.completedDate?.isBefore(settings.state.completedCutoffDate) == true) {
							tasksToDelete.add(
								task.copy(
									isDeleted = true,
									deletedDate = ZonedDateTime.now().roundToMinutes()
								)
							)
						}
					}
				println("Deleting completed tasks: $tasksToDelete")
				repository.saveTasks(tasksToDelete)
			}
		}

		repository.getAllPages()
			.onEach { list ->
				if (list.isEmpty()) {
					val main = getString(Res.string.main)
					repository.savePage(Page(title = main))
				}
				state = state.copy(pages = list)
			}.launchIn(viewModelScope)

		repository
			.getAllCategories()
			.onStart {
				val todaySorting = settings.getTodaySorting()
				val tomorrowSorting = settings.getTomorrowSorting()
				val showTodayCompleted = settings.getTodayShowCompleted()
				val showTomorrowCompleted = settings.getTomorrowShowCompleted()
				state = state.copy(
					todayShowCompleted = showTodayCompleted,
					tomorrowShowCompleted = showTomorrowCompleted,
					todayColumnSorting = todaySorting,
					tomorrowColumnSorting = tomorrowSorting
				)
			}
			.onEach { list ->
				if (list.isEmpty()) {
					repository.saveCategory(TaskCategory(
						id = 1,
						index = 3,
						isDefault = true
					))
				}
				val allTasks = list.flatMap { it.tasks }

				val todayTasks = allTasks.filter { it.dueDate?.toLocalDate() == Time.today() }.toSet()
				val tomorrowTasks = allTasks.filter { it.dueDate?.toLocalDate() == Time.tomorrow() }.toSet()
				val todayAndTomorrowTasks = todayTasks + tomorrowTasks

				val categories = list.map {
					val tasks = it.tasks.intersect(todayAndTomorrowTasks)
					it.copy(tasks = it.tasks - tasks)
				}

				state = state.copy(
					todayTasks = todayTasks.toList(),
					tomorrowTasks = tomorrowTasks.toList(),
					remainingTasks = allTasks - todayAndTomorrowTasks,
					categories = categories
				)

			}.launchIn(viewModelScope)

		checkForNewDay()

	}

	fun onAction(action: MainAction) {
		when (action) {
			is MainAction.CheckmarkToggle -> {
				val task = findTask(action.id)
				if (action.isChecked) {
					completeTask(task)
				} else {
					uncompleteTask(task)
				}
			}
			MainAction.NewColumnClick -> {
				viewModelScope.launch {
					repository.saveCategory(
						TaskCategory(
							index = state.categories.size + 1,
							pageId = state.pages[state.selectedTabIndex].id
						)
					)
				}
			}
			is MainAction.DeleteColumn -> {
				val category = state.categories.first { it.id == action.id }
				viewModelScope.launch {
					if (repository.getTaskCountForCategory(category.id) == 0) {
						repository.deleteCategory(action.id)
					} else {
						launch {
							repository.saveTasks(category.tasks.map { task ->
								repository.deleteRemindersForTask(task.id)
								task.copy(
									isDeleted = true,
									deletedDate = ZonedDateTime.now()
								)
							})
						}
						launch {
							repository.saveCategory(category.copy(isDeleted = true))
						}
					}
				}
			}
			is MainAction.SetColumnDefault -> {
				val category = state.categories.first { it.id == action.id }
				val defaultCategory = state.categories.first { it.isDefault }
				viewModelScope.launch {
					launch {
						repository.saveCategory(category.copy(isDefault = true))
					}
					launch {
						repository.saveCategory(defaultCategory.copy(isDefault = false))
					}
				}
			}
			is MainAction.ChangeColumnTitle -> {
				changeTitleJob?.cancel()
				changeTitleJob = viewModelScope.launch {
					delay(500L)
					val category = state.categories.first { it.id == action.id }
					val title = action.title.ifBlank { null }
					repository.saveCategory(category.copy(title = title))
				}
			}
			is MainAction.DeleteTask -> {
				viewModelScope.launch {
					repository.saveTask(findTask(action.id).copy(isDeleted = true, deletedDate = Time.now()))
					reminderService.cancelRemindersForTask(action.id)
				}
			}
			is MainAction.SubtaskToggle -> {
				val task = findTask(action.id)
				val subtasks = task.subtasks.mapIndexed { index, subtask ->
					if (index == action.index) subtask.copy(isCompleted = !subtask.isCompleted) else subtask
				}
				viewModelScope.launch {
					launch {
						repository.saveSubtasks(subtasks)
					}
					launch {
						if (subtasks.count { it.isCompleted } == subtasks.size) {
							completeTask(task)
						}
					}
				}
			}
			is MainAction.MoveTaskCategoryChosen -> {
				viewModelScope.launch {
					repository.saveTask(findTask(action.taskId).copy(categoryId = action.categoryId))
				}
			}
			is MainAction.SetForToday -> {
				viewModelScope.launch {
					val task = findTask(action.id)
					setDateForTask(Time.today(), task)
				}
			}
			is MainAction.SetForTomorrow -> {
				viewModelScope.launch {
					val task = findTask(action.id)
					setDateForTask(Time.tomorrow(), task)
				}
			}
			is MainAction.ClearDate -> {
				viewModelScope.launch {
					val task = findTask(action.id)
					repository.saveTask(task.copy(dueDate = null))
					reminderService.cancelRemindersForTask(task.id)
					repository.deleteRemindersForTask(task.id)
				}
			}
			is MainAction.NewTaskClick -> {
//				backstack.add(EditGraph(null, action.categoryId))
//				println("Added edit graph to backstack $backstack")
			}
			is MainAction.OnClick -> {
				backstack.add(EditGraph(action.id))
			}
			is MainAction.ReorderTasks -> {
				viewModelScope.launch {
					repository.saveTasks(action.tasks)
				}
			}
			is MainAction.ChangeColumnSorting -> {
				viewModelScope.launch {
					when (action.id) {
						-1L -> {
							settings.saveTodaySorting(action.sorting)
							state = state.copy(todayColumnSorting = action.sorting)
						}
						-2L -> {
							settings.saveTomorrowSorting(action.sorting)
							state = state.copy(tomorrowColumnSorting = action.sorting)
						}
						else -> {
							val category = state.categories.first { it.id == action.id }
							repository.saveCategory(category.copy(sorting = action.sorting))
						}
					}
				}
			}
			is MainAction.ChangeColumnCompletedView -> {
				viewModelScope.launch {
					when (action.id) {
						-1L -> {
							settings.saveTodayCompletedShowing(action.isShowing)
							state = state.copy(todayShowCompleted = action.isShowing)
						}
						-2L -> {
							settings.saveTomorrowCompletedShowing(action.isShowing)
							state = state.copy(tomorrowShowCompleted = action.isShowing)
						}
						else -> {
							val category = state.categories.first { it.id == action.id }
							repository.saveCategory(category.copy(completedOpen = action.isShowing))
						}
					}
				}
			}
			is MainAction.ChangeColumnsIndices -> {
				viewModelScope.launch {
					repository.saveCategories(action.categories)
				}
			}
			is MainAction.NewTabClick -> {
				viewModelScope.launch {
					repository.savePage(Page())
				}
			}
			is MainAction.OnTabClick -> {
				state = state.copy(
					selectedTabIndex = action.tabIndex
				)
			}
			is MainAction.DeleteTabClick -> {
				val categories = state.categories
					.filter { it.pageId == action.id }
					.map { it.copy(pageId = 1) }
				val page = state.pages.first { it.id == action.id }
				if (state.pages.indexOf(page) == state.selectedTabIndex) {
					state = state.copy(selectedTabIndex = 0)
				}
				viewModelScope.launch {
					repository.saveCategories(categories)
					repository.deletePage(action.id)
				}

			}
			is MainAction.TabRename -> {
				val page = state.pages.first { it.id == action.id }
				viewModelScope.launch {
					repository.savePage(page.copy(title = action.title))
				}
			}
			is MainAction.MoveCategoryPageChosen -> {
				viewModelScope.launch {
					val category = state.categories.first { it.id == action.categoryId }
					repository.saveCategory(category.copy(pageId = action.pageId))
				}
			}
			is MainAction.SavePages -> {
				viewModelScope.launch {
					action.list.forEach { page ->
						repository.savePage(page)
					}
				}
			}
			else -> Unit
		}
	}

	private fun checkForNewDay() {
		viewModelScope.launch {
			if (settings.checkForNewDay()) {
				backstack.add(NewDayRoute)
			}
		}
	}

	private fun completeTask(task: Task) {
		viewModelScope.launch {
			if (task.repeatState.isRepeating
				&& (task.dueDate?.isBefore(Time.todayStart()) == true
					|| task.calculateNewDate(settings.state.firstDayOfWeek)?.isBefore(Time.todayEnd()) == true)) {
						repository.saveTask(task.copy(
							isCompleted = false,
							dueDate = task.calculateNewDate(settings.state.firstDayOfWeek)
						))
						rescheduleRemindersForTask(task)
			} else {
				repository.saveTask(task.copy(isCompleted = true, completedDate = Time.now()))
				reminderService.cancelRemindersForTask(task.id)
			}
		}

		// Sound source: https://freesound.org/people/LittleRainySeasons/sounds/335908/
		audioPlayer.play("files/ding.wav")
	}

	private fun uncompleteTask(task: Task) {
		viewModelScope.launch {
			repository.saveTask(task.copy(isCompleted = false, completedDate = null))
			task.dueDate?.let {
				rescheduleRemindersForTask(task)
			}
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

	private suspend fun setDateForTask(date: LocalDate, task: Task) {
		val isAllDay = if (task.dueDate == null) true else task.isAllDay
		val time = task.dueDate?.toLocalTime() ?: LocalTime.NOON
		val newDate = date.atTime(time).atZone(ZoneId.systemDefault())

		val reminders = repository.getRemindersForTask(task.id).toMutableList()
		if (reminders.isEmpty()) {
			reminders.add(Reminder(parentTaskId = task.id))
		}
		repository.saveReminders(reminders)

		repository.saveTask(task.copy(isAllDay = isAllDay, dueDate = newDate))

		rescheduleRemindersForTask(task)
	}

	fun findTask(id: Long): Task {
		return state.allTasks.first {
			it.id == id
		}
	}
}