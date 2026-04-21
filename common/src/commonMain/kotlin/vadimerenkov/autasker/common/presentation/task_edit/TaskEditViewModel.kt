package vadimerenkov.autasker.common.presentation.task_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vadimerenkov.autasker.common.domain.Period
import vadimerenkov.autasker.common.domain.Subtask
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.TasksRepository
import vadimerenkov.autasker.common.domain.Time
import vadimerenkov.autasker.common.domain.minusReminder
import vadimerenkov.autasker.common.domain.reminders.Reminder
import vadimerenkov.autasker.common.domain.reminders.ReminderService
import vadimerenkov.autasker.common.presentation.task_edit.calendar.DateTimeState
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class TaskEditViewModel(
	val id: Long?,
	val categoryId: Long? = null,
	private val repository: TasksRepository,
	private val reminderService: ReminderService
): ViewModel() {

	var state by mutableStateOf(TaskEditState())
		private set

	var dateTimeState by mutableStateOf(DateTimeState())
		private set

	init {
		viewModelScope.launch {
			state = state.copy(
				id = id ?: 0,
				categories = repository.getAllCategories().first()
			)

			val assignedCategoryId = when (categoryId) {
				-1L, -2L, null, 0L -> state.categories.first { it.isDefault }.id
				else -> categoryId
			}
			println("Category id was passed as $categoryId and converted to $assignedCategoryId.")
			val newTask = if (id == null) {
				Task(
					id = state.id,
					categoryId = assignedCategoryId,
					title = "",
					dueDate = setNewDate(categoryId)
				)
			} else {
				repository.getTask(id)
			}
			val reminders = repository.getRemindersForTask(newTask.id)
			println("Loading new task: $newTask with reminders: $reminders")
			loadTask(newTask.copy(reminders = reminders))
		}
	}

	fun onAction(action: TaskEditAction) {
		when (action) {

			// normal actions
			is TaskEditAction.CompletedToggle -> {
				state = state.copy(isCompleted = action.isCompleted)
			}
			is TaskEditAction.DescriptionChange -> {
				state = state.copy(description = action.description)
			}
			is TaskEditAction.TitleChange -> {
				state = state.copy(title = action.title)
			}
			TaskEditAction.ConfirmCalendarChanges -> {
				state = state.copy(
					dueDate = if (dateTimeState.hasDate) dateTimeState.dateTime else null,
					isAllDay = !dateTimeState.hasTime,
					repeatState = dateTimeState.repeatState,
					reminders = dateTimeState.reminders
				)
				dateTimeState = dateTimeState.copy(isOpen = false)
			}
			TaskEditAction.CalendarToggle -> {
				val date = state.dueDate?.toLocalDate() ?: LocalDate.now()
				val time = state.dueDate?.toLocalTime() ?: LocalTime.now().plusMinutes(10)

				dateTimeState = if (!dateTimeState.isOpen) {
					DateTimeState(
						isOpen = true,
						date = date,
						time = time,
						isRepeated = state.repeatState.isRepeating,
						repeatMode = state.repeatState.mode,
						times = state.repeatState.times,
						period = state.repeatState.period,
						hasTime = !state.isAllDay,
						weekdays = state.repeatState.weekDays,
						reminders = state.reminders.ifEmpty {
							listOf(
								Reminder(
									parentTaskId = state.id,
								)
							)
						}
					)

				} else {
					dateTimeState.copy(isOpen = false)
				}
			}
			TaskEditAction.DeleteDate -> {
				state = state.copy(dueDate = null)
			}
			TaskEditAction.DeleteRepeating -> {
				state = state.copy(repeatState = state.repeatState.copy(isRepeating = false))
			}
			is TaskEditAction.DeleteSubtask -> {
				state = state.copy(subtasks = state.subtasks.minus(state.subtasks[action.index]))
			}
			is TaskEditAction.SaveSubtask -> {
				val subtask = Subtask(
					title = action.title,
					index = state.subtasks.size + 1,
					parentTaskId = state.id
				)
				state = state.copy(subtasks = state.subtasks.plus(subtask))
			}
			is TaskEditAction.SubtaskToggle -> {
				state = state.copy(subtasks = state.subtasks.mapIndexed { index, subtask ->
					if (index == action.index) {
						subtask.copy(isCompleted = !subtask.isCompleted)
					} else subtask
				})
			}
			is TaskEditAction.ImportanceChanged -> {
				state = state.copy(importance = action.int)
			}
			is TaskEditAction.CategorySelected -> {
				state = state.copy(categoryId = action.id, tag = state.categories.first { it.id == action.id }.title)
			}
			is TaskEditAction.CommonDatePicked -> {
				val date = when (action.date) {
					1 -> Time.today()
					else -> Time.tomorrow()
				}
				state = state.copy(dueDate = date.atStartOfDay(ZoneId.systemDefault()))
			}


			// calendar actions
			is TaskEditAction.AddReminder -> {
				dateTimeState = dateTimeState.copy(
					reminders = dateTimeState.reminders + action.reminder
				)
			}
			is TaskEditAction.RemoveReminder -> {
				dateTimeState = dateTimeState.copy(
					reminders = dateTimeState.reminders - action.reminder
				)
			}
			is TaskEditAction.HasDateToggle -> {
				dateTimeState = dateTimeState.copy(
					hasDate = action.hasDate
				)
			}
			is TaskEditAction.HasTimeToggle -> {
				val period = if ((!action.hasTime) && (dateTimeState.period == Period.MINUTE || dateTimeState.period == Period.HOUR)) {
					null
				} else {
					dateTimeState.period
				}
				dateTimeState = dateTimeState.copy(hasTime = action.hasTime, period = period)
			}
			is TaskEditAction.DateChange -> {
				dateTimeState = dateTimeState.copy(date = action.date)
			}
			is TaskEditAction.TimeChange -> {
				dateTimeState = dateTimeState.copy(time = action.time)
			}
			is TaskEditAction.PeriodChange -> {
				val weekdays = if (action.period == Period.WEEK && dateTimeState.weekdays.isEmpty()) {
					listOf(Time.now().dayOfWeek)
				} else dateTimeState.weekdays
				dateTimeState = dateTimeState.copy(period = action.period, weekdays = weekdays)
			}
			is TaskEditAction.RepeatModeChange -> {
				dateTimeState = dateTimeState.copy(repeatMode = action.mode)
			}
			is TaskEditAction.RepeatToggle -> {
				dateTimeState = dateTimeState.copy(isRepeated = action.repeat)
			}
			is TaskEditAction.TimesChange -> {
				dateTimeState = dateTimeState.copy(times = action.times)
			}
			is TaskEditAction.WeekdaySelected -> {
				dateTimeState = dateTimeState.copy(
					weekdays = if (dateTimeState.weekdays.contains(action.weekday)) {
						dateTimeState.weekdays.minus(action.weekday)
					} else {
						dateTimeState.weekdays.plus(action.weekday)
					}
				)
			}
		}
	}

	fun loadTask(task: Task) {
		viewModelScope.launch {
			state = state.copy(
				id = task.id,
				categoryId = task.categoryId,
				index = task.index,
				title = task.title,
				description = task.description,
				dueDate = task.dueDate,
				repeatState = task.repeatState,
				isCompleted = task.isCompleted,
				completedDate = task.completedDate,
				isDeleted = task.isDeleted,
				deletedDate = task.deletedDate,
				isAllDay = task.isAllDay,
				importance = task.importance,
				subtasks = task.subtasks,
				reminders = task.reminders,
				isLoading = false,

				tag = state.categories.first { it.id == task.categoryId }.title
			)
		}
	}

	suspend fun saveTask() {
		val task = Task(
			id = state.id,
			categoryId = state.categoryId,
			index = state.index,
			title = state.title,
			description = state.description,
			dueDate = state.dueDate,
			repeatState = state.repeatState,
			isCompleted = state.isCompleted,
			completedDate = state.completedDate,
			isDeleted = state.isDeleted,
			deletedDate = state.deletedDate,
			isAllDay = state.isAllDay,
			importance = state.importance,
			reminders = state.reminders,
		)
		val taskId = repository.saveTask(task)
		val parentTaskId = if (taskId == -1L) state.id else taskId

		println("Task id is $taskId, parent id = $parentTaskId")
		repository.deleteSubtasksForTask(parentTaskId)

		val subtasks = state.subtasks.map {
			it.copy(parentTaskId = parentTaskId)
		}

		reminderService.cancelRemindersForTask(parentTaskId)
		val reminders = state.reminders.map {
			val date = state.dueDate?.minusReminder(it)!!
			reminderService.scheduleReminder(parentTaskId, date)
			it.copy(parentTaskId = parentTaskId)
		}
		println("Saving reminders: $reminders")
		println("Saving subtasks: $subtasks")

		repository.deleteRemindersForTask(parentTaskId)
		repository.saveReminders(reminders)
		repository.saveSubtasks(subtasks)
	}

	private fun setNewDate(categoryId: Long?): ZonedDateTime? {
		return when (categoryId) {
			-1L -> Time.now().plusMinutes(10)
			-2L -> Time.now().plusDays(1).plusMinutes(10)
			else -> null
		}
	}
}