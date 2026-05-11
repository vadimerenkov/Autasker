package vadimerenkov.autasker.habits.presentation.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vadimerenkov.autasker.habits.domain.HabitsRepository

class HabitEditViewModel(
	private val id: Long,
	private val repository: HabitsRepository
): ViewModel() {

	var state by mutableStateOf(HabitEditState())
		private set

	init {
		viewModelScope.launch {
			val habit = repository.getHabit(id)
			state = HabitEditState(
				title = habit.title,
				times = habit.times,
				period = habit.period,
				type = habit.type,
				customQuantifier = habit.customQuantifier ?: ""
			)
		}
	}

	fun onAction(action: HabitEditAction) {
		when(action) {
			is HabitEditAction.OnTitleChange -> {
				state = state.copy(title = action.title)
			}
		}
	}
}