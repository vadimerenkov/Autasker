package vadimerenkov.autasker.habits.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.habits.HabitsRepository

class HabitsViewModel(
	private val repository: HabitsRepository
): ViewModel() {

	var state by mutableStateOf(HabitsState())
		private set

	init {
		repository.getAllHabits()
			.onEach { list ->
				state = state.copy(habits = list)
			}.launchIn(viewModelScope)

		repository.getAllCompletions()
			.onEach {
				state = state.copy(completions = it)
			}.launchIn(viewModelScope)
	}

	fun onAction(action: HabitsAction) {
		when (action) {
			is HabitsAction.OnHabitClick -> {
				state = state.copy(selectedHabit = state.habits.find { it.id == action.id })
			}
			is HabitsAction.OnCalendarDayClick -> {
				state = state.copy(openedCalendarDay = action.date)
			}
			is HabitsAction.DayDialogDismiss -> {
				state = state.copy(openedCalendarDay = null)
			}
			is HabitsAction.DeleteHabitClick -> {
				val habit = state.habits.find { it.id == action.id }
				state = state.copy(isDeleteDialogOpen = true, deletingHabit = habit)
			}
			is HabitsAction.DismissDeleteDialogClick -> {
				state = state.copy(isDeleteDialogOpen = false, deletingHabit = null)
			}
			is HabitsAction.ConfirmDeletion -> {
				viewModelScope.launch {
					state.deletingHabit?.let {
						repository.deleteHabit(it.id)
						repository.deleteCompletionsForHabit(it.id)
					}
				}
				state = state.copy(isDeleteDialogOpen = false, deletingHabit = null)
			}
			else -> Unit
		}
	}
}