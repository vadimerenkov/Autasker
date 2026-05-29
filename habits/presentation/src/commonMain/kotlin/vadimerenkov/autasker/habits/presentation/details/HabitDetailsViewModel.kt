package vadimerenkov.autasker.habits.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.habits.HabitsRepository
import vadimerenkov.autasker.habits.domain.HabitTracker

class HabitDetailsViewModel(
	id: Long,
	private val repository: HabitsRepository,
	private val habitTracker: HabitTracker
): ViewModel() {

	var state by mutableStateOf(HabitDetailsState())
		private set

	init {
		viewModelScope.launch {
			val habit = repository.getHabit(id)
			state = state.copy(habit = habit)
		}

		repository
			.getCompletionsForHabit(id)
			.onEach { completions ->
				val dates = habitTracker.calculateDatePeriods(state.habit, completions)
				val currentStreak = habitTracker.calculateCurrentStreak(state.habit, completions, dates)
				state = state.copy(
					completions = completions,
					dates = dates,
					currentStreak = currentStreak
				)
			}.launchIn(viewModelScope)
	}

	fun onAction(action: HabitDetailsAction) {
		when (action) {
			is HabitDetailsAction.OnCalendarDayClick -> {
				state = state.copy(openedCalendarDay = action.date)
			}
			is HabitDetailsAction.DayDialogDismiss -> {
				state = state.copy(openedCalendarDay = null)
			}
			is HabitDetailsAction.DayDialogSave -> {
				viewModelScope.launch {
					action.completions.forEach { completion ->
						repository.saveCompletion(completion)
					}
				}
			}
			is HabitDetailsAction.DeleteCompletions -> {
				viewModelScope.launch {
					action.completions.forEach { completion ->
						repository.deleteCompletion(completion.id)
					}
				}
			}
		}
	}

}