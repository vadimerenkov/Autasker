package vadimerenkov.autasker.habits.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.habits.domain.HabitsRepository

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
	}

	fun onAction(action: HabitsAction) {
		when (action) {
			is HabitsAction.NewHabitClick -> {
				viewModelScope.launch {
					repository.saveHabit(Habit(title = "ASDF"))
				}
			}
		}
	}
}