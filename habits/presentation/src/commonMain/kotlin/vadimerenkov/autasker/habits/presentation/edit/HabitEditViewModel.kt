package vadimerenkov.autasker.habits.presentation.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.habits.domain.HabitsRepository

class HabitEditViewModel(
	private val id: Long?,
	private val repository: HabitsRepository
): ViewModel() {

	var state by mutableStateOf(HabitEditState())
		private set

	init {
		viewModelScope.launch {
			id?.let {
				val habit = repository.getHabit(it)
				state = HabitEditState(
					title = habit.title,
					times = habit.times,
					period = habit.period,
					type = habit.type,
					customQuantifier = habit.customQuantifier ?: ""
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
					repository.saveHabit(habit)
				}
			}
		}
	}
}