package vadimerenkov.autasker.core.presentation.new_day

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.RepeatMode
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.domain.TasksRepository
import vadimerenkov.autasker.core.domain.Time
import vadimerenkov.autasker.core.domain.minusReminder
import vadimerenkov.autasker.core.domain.reminders.ReminderService
import vadimerenkov.autasker.core.domain.settings.Settings
import java.time.ZoneId

class NewDayViewModel(
	private val repository: TasksRepository,
	private val settings: Settings,
	private val backStack: MutableList<Any>,
	private val reminderService: ReminderService
): ViewModel() {
	var state by mutableStateOf(NewDayState())

	init {
		viewModelScope.launch {
			val yesterdayTasks = repository.getYesterdayUncompletedTasks()
			val todayTasks = repository.getUncompletedDatelessTasks()
			val tabs = mutableListOf<Tab>()
			if (yesterdayTasks.isNotEmpty()) {
				tabs.add(Tab.YESTERDAY_TASKS)
			}
			if (todayTasks.isNotEmpty()) {
				tabs.add(Tab.TODAY_TASKS)
			}
			if (tabs.isEmpty()) {
				finish()
			}
			state = state.copy(
				yesterdayTasks = yesterdayTasks,
				todayTasks = todayTasks,
				listOfTabs = tabs.toList(),
				currentTab = tabs.firstOrNull()
			)
		}
	}

	fun onAction(action: NewDayAction) {
		when (action) {
			NewDayAction.NextButtonClick -> {
				if (state.currentTab == state.listOfTabs.lastOrNull()) {
					finish()
				} else {
					state = state.copy(
						currentTab = state.listOfTabs[state.currentTabIndex + 1]
					)
				}
			}
			NewDayAction.PreviousButtonClick -> {
				state = state.copy(
					currentTab = state.listOfTabs[state.currentTabIndex - 1]
				)
			}
			is NewDayAction.TaskClick -> {
				when (state.currentTab) {
					Tab.YESTERDAY_TASKS -> {
						val yesterdayTasks = state
							.yesterdayTasks
							.map { task ->
								if (task.id == action.id) {
									if (task.isCompleted) {
										task.copy(isCompleted = false, completedDate = null)
									} else {
										task.copy(isCompleted = true, completedDate = Time.yesterday().atStartOfDay(
											ZoneId.systemDefault()), dueDate = Time.yesterday().atTime(task.dueDate?.toLocalTime()).atZone(ZoneId.systemDefault()))
									}
								} else task
							}
						state = state.copy(yesterdayTasks = yesterdayTasks)
					}
					Tab.TODAY_TASKS -> {
						val todayTasks = state
							.todayTasks
							.map { task ->
								if (task.id == action.id) {
									if (task.dueDate == null) {
										task.copy(dueDate = Time.now())
									} else {
										task.copy(dueDate = null)
									}
								} else task
							}
						state = state.copy(todayTasks = todayTasks)
					}
					null -> {

					}
				}
			}

			is NewDayAction.SetTodayClick -> {
				val yesterdayTasks = state
					.yesterdayTasks
					.map { task ->
						if (task.id == action.id) {
							if (action.isSet) {
								task.copy(dueDate = Time.today().atTime(task.dueDate?.toLocalTime()).atZone(ZoneId.systemDefault()), isCompleted = false, completedDate = null)
							} else {
								task.copy(dueDate = Time.yesterday().atTime(task.dueDate?.toLocalTime()).atZone(ZoneId.systemDefault()))
							}
						} else task
					}
				state = state.copy(yesterdayTasks = yesterdayTasks)
			}
		}
	}

	private fun finish() {
		val tasks = state.yesterdayTasks + state.todayTasks
		viewModelScope.launch {
			tasks.forEach { task ->
				task.dueDate?.let {
					reminderService.cancelRemindersForTask(task.id)
					task.reminders.forEach { reminder ->
						val date = it.minusReminder(reminder)
						reminderService.scheduleReminder(task.id, date)
					}
				}
			}
			repository.saveTasks(tasks)

			val repeatingTasks = repository.getRepeatingTasks()
			println("There are ${repeatingTasks.size} repeating tasks")
			val updatedTasks = mutableListOf<Task>()

			val jobs = repeatingTasks.map { task ->
				launch() {
					when (task.repeatState.mode) {
						RepeatMode.ON_COMPLETION, RepeatMode.ON_EXACT -> {
							if (task.isCompleted) {
								updatedTasks.add(task.copy(
									dueDate = task.calculateNewDate(settings.state.value.firstDayOfWeek),
									isCompleted = false,
									completedDate = null
								))
							}
						}
						/*
						RepeatMode.FORGIVING -> {
							with (Dispatchers.Default) {
								var count = 0

								var dateTime = task.dueDate
								do {
									dateTime = dateTime?.calculateNewDate(task.repeatState)
									count++
								} while (dateTime?.isBefore(Time.todayStart()) == true)

								do {
									updatedTasks.add(
										task.copy(
											id = UUID.randomUUID(),
											dueDate = dateTime,
											isCompleted = false
										)
									)
									dateTime = dateTime?.calculateNewDate(task.repeatState)

								} while (dateTime?.isAfter(Time.todayStart()) == true && dateTime.isBefore(Time.tomorrowEnd()))

								if (count > 0) {
									repository.deleteTasks(listOf(task))
								}
							}

						}
						RepeatMode.ALWAYS -> {
							var dateTime = task.dueDate
							do {
								dateTime = dateTime?.calculateNewDate(task.repeatState)

								updatedTasks.add(
									task.copy(
										id = UUID.randomUUID(),
										dueDate = dateTime,
										isCompleted = false
									)
								)
							} while (dateTime?.isAfter(Time.todayStart()) == true && dateTime.isBefore(Time.tomorrowEnd()))
						}

						 */
					}
				}
			}

			jobs.joinAll()
			println("Finished updating tasks: $updatedTasks")
			updatedTasks.forEach { task ->
				task.dueDate?.let {
					reminderService.cancelRemindersForTask(task.id)
					println("Canceled previous reminders")
					task.reminders.forEach { reminder ->
						val date = it.minusReminder(reminder)
						reminderService.scheduleReminder(task.id, date)
						println("Scheduled reminder $reminder")
					}
				}
			}
			repository.saveTasks(updatedTasks)
			backStack.removeLastOrNull()
		}

	}
}

enum class Tab {
	YESTERDAY_TASKS,
	TODAY_TASKS
}