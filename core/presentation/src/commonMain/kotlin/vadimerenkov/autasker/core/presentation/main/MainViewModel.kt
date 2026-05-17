package vadimerenkov.autasker.core.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import vadimerenkov.autasker.core.domain.Page
import vadimerenkov.autasker.core.domain.Subtask
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.domain.TaskCategory
import vadimerenkov.autasker.core.domain.TasksRepository
import vadimerenkov.autasker.core.domain.Time
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import vadimerenkov.autasker.core.domain.habits.HabitsRepository
import vadimerenkov.autasker.core.domain.minusReminder
import vadimerenkov.autasker.core.domain.reminders.Reminder
import vadimerenkov.autasker.core.domain.reminders.ReminderService
import vadimerenkov.autasker.core.domain.roundToMinutes
import vadimerenkov.autasker.core.domain.settings.Settings
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class MainViewModel(
	private val tasksRepository: TasksRepository,
	private val habitsRepository: HabitsRepository,
	private val reminderService: ReminderService,
	private val audioPlayer: AudioPlayer,
	private val settings: Settings
): ViewModel() {

	var state by mutableStateOf(MainState())
		private set

	private var changeTitleJob: Job? = null

	init {
		println("Today start is ${Time.todayStart()}")
		println("Today end is ${Time.todayEnd()}")
		println("Tomorrow start is ${Time.tomorrowStart()}")
		println("Tomorrow end is ${Time.tomorrowEnd()}")
		if (settings.state.value.autoDeleteFromTrash) {
			val tasksToDelete = mutableListOf<Task>()
			val subtasksToDelete = mutableListOf<Subtask>()
			viewModelScope.launch {
				tasksRepository.getDeletedTasks()
					.first()
					.forEach { task ->
						val deletedDate = task.deletedDate
						if (deletedDate?.isBefore(settings.state.value.deletedCutoffDate) == true) {
							tasksToDelete.add(task)
							subtasksToDelete.addAll(task.subtasks)
						}
					}
				println("Deleting overdue tasks: $tasksToDelete")
				launch {
					tasksRepository.deleteTasks(tasksToDelete)
				}
				launch {
					tasksRepository.deleteSubtasks(subtasksToDelete)
				}
			}
		}

		if (settings.state.value.autoDeleteCompleted) {
			val tasksToDelete = mutableListOf<Task>()
			viewModelScope.launch {
				tasksRepository.getCompletedTasks()
					.forEach { task ->
						if (task.completedDate?.isBefore(settings.state.value.completedCutoffDate) == true) {
							tasksToDelete.add(
								task.copy(
									isDeleted = true,
									deletedDate = ZonedDateTime.now().roundToMinutes()
								)
							)
						}
					}
				println("Deleting completed tasks: $tasksToDelete")
				tasksRepository.saveTasks(tasksToDelete)
			}
		}

		tasksRepository.getAllPages()
			.onEach { list ->
				if (list.isEmpty()) {
					val main = getString(Res.string.main)
					tasksRepository.savePage(Page(title = main))
				}
				state = state.copy(pages = list)
			}.launchIn(viewModelScope)

		tasksRepository
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
					tasksRepository.saveCategory(
						TaskCategory(
							id = 1,
							index = 3,
							isDefault = true
						)
					)
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
					tasksRepository.saveCategory(
						TaskCategory(
							index = state.categories.size + 3,
							pageId = state.pages[state.selectedTabIndex].id
						)
					)
				}
			}
			is MainAction.DeleteColumn -> {
				val category = state.categories.first { it.id == action.id }
				viewModelScope.launch {
					if (tasksRepository.getTaskCountForCategory(category.id) == 0) {
						tasksRepository.deleteCategory(action.id)
					} else {
						launch {
							tasksRepository.saveTasks(category.tasks.map { task ->
								tasksRepository.deleteRemindersForTask(task.id)
								task.copy(
									isDeleted = true,
									deletedDate = ZonedDateTime.now()
								)
							})
						}
						launch {
							tasksRepository.saveCategory(category.copy(isDeleted = true))
						}
					}
				}
			}
			is MainAction.SetColumnDefault -> {
				val category = state.categories.first { it.id == action.id }
				val defaultCategory = state.categories.first { it.isDefault }
				viewModelScope.launch {
					launch {
						tasksRepository.saveCategory(category.copy(isDefault = true))
					}
					launch {
						tasksRepository.saveCategory(defaultCategory.copy(isDefault = false))
					}
				}
			}
			is MainAction.ChangeColumnTitle -> {
				changeTitleJob?.cancel()
				changeTitleJob = viewModelScope.launch {
					delay(500L)
					val category = state.categories.first { it.id == action.id }
					val title = action.title.ifBlank { null }
					tasksRepository.saveCategory(category.copy(title = title))
				}
			}
			is MainAction.DeleteTask -> {
				viewModelScope.launch {
					tasksRepository.saveTask(findTask(action.id).copy(isDeleted = true, deletedDate = Time.now()))
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
						tasksRepository.saveSubtasks(subtasks)
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
					tasksRepository.saveTask(findTask(action.taskId).copy(categoryId = action.categoryId))
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
					tasksRepository.saveTask(task.copy(dueDate = null))
					reminderService.cancelRemindersForTask(task.id)
					tasksRepository.deleteRemindersForTask(task.id)
				}
			}
			is MainAction.ReorderTasks -> {
				viewModelScope.launch {
					tasksRepository.saveTasks(action.tasks)
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
							tasksRepository.saveCategory(category.copy(sorting = action.sorting))
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
							tasksRepository.saveCategory(category.copy(completedOpen = action.isShowing))
						}
					}
				}
			}
			is MainAction.ChangeColumnsIndices -> {
				viewModelScope.launch {
					tasksRepository.saveCategories(action.categories)
				}
			}
			is MainAction.NewTabClick -> {
				viewModelScope.launch {
					tasksRepository.savePage(Page(index = state.pages.size + 1))
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
					tasksRepository.saveCategories(categories)
					tasksRepository.deletePage(action.id)
				}

			}
			is MainAction.TabRename -> {
				val page = state.pages.first { it.id == action.id }
				viewModelScope.launch {
					tasksRepository.savePage(page.copy(title = action.title))
				}
			}
			is MainAction.MoveCategoryPageChosen -> {
				viewModelScope.launch {
					val category = state.categories.first { it.id == action.categoryId }
					tasksRepository.saveCategory(category.copy(pageId = action.pageId))
				}
			}
			is MainAction.SavePages -> {
				viewModelScope.launch {
					action.list.forEach { page ->
						tasksRepository.savePage(page)
					}
				}
			}
			is MainAction.SkipTask -> {
				val task = findTask(action.id)
				viewModelScope.launch {
					tasksRepository.saveTask(task.copy(dueDate = task.calculateNewDate(settings.state.value.firstDayOfWeek)))
				}
			}
			is MainAction.SaveHabitCompletionClick -> {
				val completion = HabitCompletion(
					habitId = action.habitId,
					date = Time.now(),
					quantity = action.times
				)
				viewModelScope.launch {
					habitsRepository.saveCompletion(completion)
				}
				state = state.copy(
					showHabitCompletionDialog = false
				)
			}
			MainAction.DismissHabitDialog -> {
				state = state.copy(
					showHabitCompletionDialog = false
				)
			}
			else -> Unit
		}
	}

	private fun completeTask(task: Task) {
		viewModelScope.launch {
			task.habitId?.let {
				state = state.copy(
					showHabitCompletionDialog = true,
					dialogHabit = habitsRepository.getHabit(it)
				)
			}

			if (task.repeatState.isRepeating
				&& task.calculateNewDate(settings.state.value.firstDayOfWeek)?.isBefore(Time.todayEnd()) == true) {
					val newTask = task.copy(
						isCompleted = false,
						dueDate = task.calculateNewDate(settings.state.value.firstDayOfWeek)
					)
					tasksRepository.saveTask(newTask)
					rescheduleRemindersForTask(newTask)
			} else {
				tasksRepository.saveTask(task.copy(isCompleted = true, completedDate = Time.now()))
				reminderService.cancelRemindersForTask(task.id)
			}


		}

		if (settings.state.value.playSound) {
			// Sound source: https://freesound.org/people/LittleRainySeasons/sounds/335908/
			audioPlayer.play("files/ding.wav")
		}
	}

	private fun uncompleteTask(task: Task) {
		viewModelScope.launch {
			tasksRepository.saveTask(task.copy(isCompleted = false, completedDate = null))
			task.dueDate?.let {
				rescheduleRemindersForTask(task)
			}
		}
	}

	private suspend fun rescheduleRemindersForTask(task: Task) {
		reminderService.cancelRemindersForTask(task.id)
		tasksRepository
			.getRemindersForTask(task.id)
			.forEach { reminder ->
				if (task.dueDate?.minusReminder(reminder)?.isBefore(Time.now()) == false) {
					reminderService.scheduleReminder(task.id, task.dueDate!!.minusReminder(reminder))
				}
			}
	}

	private suspend fun setDateForTask(date: LocalDate, task: Task) {
		val isAllDay = if (task.dueDate == null) true else task.isAllDay
		val time = task.dueDate?.toLocalTime() ?: LocalTime.NOON
		val newDate = date.atTime(time).atZone(ZoneId.systemDefault())

		val reminders = tasksRepository.getRemindersForTask(task.id).toMutableList()
		if (reminders.isEmpty()) {
			reminders.add(Reminder(parentTaskId = task.id))
		}
		tasksRepository.saveReminders(reminders)

		tasksRepository.saveTask(task.copy(isAllDay = isAllDay, dueDate = newDate))

		rescheduleRemindersForTask(task)
	}

	private fun findTask(id: Long): Task {
		return state.allTasks.first {
			it.id == id
		}
	}
}