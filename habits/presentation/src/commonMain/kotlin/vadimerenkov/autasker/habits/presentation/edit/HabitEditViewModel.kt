package vadimerenkov.autasker.habits.presentation.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.TasksRepository
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.habits.domain.HabitsRepository

class HabitEditViewModel(
	private val id: Long?,
	private val habitsRepository: HabitsRepository,
	private val tasksRepository: TasksRepository
): ViewModel() {

	var state by mutableStateOf(HabitEditState())
		private set

	init {
		viewModelScope.launch {
			val allTasks = tasksRepository.getAllTasks().first()
			if (id == null) {
				state = state.copy(
					allTasks = allTasks
				)
			} else {
				val habit = habitsRepository.getHabit(id)
				val tasks = tasksRepository.getTasksForHabit(id)
				state = HabitEditState(
					title = habit.title,
					times = habit.times,
					period = habit.period,
					type = habit.type,
					customQuantifier = habit.customQuantifier ?: "",
					tasks = tasks,
					allTasks = allTasks - tasks.toSet()
				)
			}
		}
	}

	fun onAction(action: HabitEditAction) {
		when(action) {
			is HabitEditAction.TitleChange -> {
				state = state.copy(title = action.title)
			}
			is HabitEditAction.PeriodChange -> {
				state = state.copy(period = action.period)
			}
			is HabitEditAction.QuantifierChange -> {
				state = state.copy(customQuantifier = action.quantifier)
			}
			is HabitEditAction.TimesChange -> {
				state = state.copy(times = action.times)
			}
			is HabitEditAction.TypeChange -> {
				state = state.copy(type = action.type)
			}
			HabitEditAction.OnSaveClick -> {
				viewModelScope.launch {
					val habit = Habit(
						id = id ?: 0,
						title = state.title,
						period = state.period,
						times = state.times ?: 1,
						type = state.type,
						customQuantifier = state.customQuantifier.ifBlank { null }
					)
					val savedId = habitsRepository.saveHabit(habit)
					val habitId = id ?: savedId
					val tasks = state.tasks.map { task ->
						task.copy(habitId = habitId)
					}
					tasksRepository.saveTasks(tasks)
				}
			}
			is HabitEditAction.AddTaskClick -> {
				state = state.copy(
					tasks = state.tasks + action.task,
					allTasks = state.allTasks - action.task
				)
			}
			is HabitEditAction.RemoveTaskClick -> {
				state = state.copy(
					tasks = state.tasks - action.task,
					allTasks = state.allTasks + action.task
				)
			}
		}
	}
}