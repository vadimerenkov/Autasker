package vadimerenkov.autasker.habits.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.Time
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import vadimerenkov.autasker.core.domain.habits.HabitsRepository
import vadimerenkov.autasker.habits.domain.di.DatePeriod
import vadimerenkov.autasker.habits.domain.di.isIn

class HabitDetailsViewModel(
	id: Long,
	private val repository: HabitsRepository
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
				val dates = calculateDatePeriods(completions)
				val currentStreak = calculateCurrentStreak(completions, dates)
				val totalCompletions = calculateTotalCompletions(completions)
				state = state.copy(
					completions = completions,
					dates = dates,
					currentStreak = currentStreak,
					totalCompletions = totalCompletions
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
		}
	}

	fun calculateDatePeriods(completions: List<HabitCompletion>): List<DatePeriod> {
		if (completions.isEmpty()) return listOf()

		val dates = mutableListOf<DatePeriod>()
		var endingDate = Time.now()
		var startingDate = when (state.habit.period) {
			Period.MINUTE,
			Period.HOUR -> throw IllegalStateException()
			Period.DAY -> endingDate.minusDays(1)
			Period.WEEK -> endingDate.minusWeeks(1)
			Period.MONTH -> endingDate.minusMonths(1)
			Period.YEAR -> endingDate.minusYears(1)
		}

		val lastDate = DatePeriod(startingDate = startingDate, endingDate = endingDate)
		dates.add(lastDate)

		while (completions.first().date.isBefore(startingDate)) {
			endingDate = startingDate
			startingDate = when (state.habit.period) {
				Period.MINUTE,
				Period.HOUR -> throw IllegalStateException()
				Period.DAY -> endingDate.minusDays(1)
				Period.WEEK -> endingDate.minusWeeks(1)
				Period.MONTH -> endingDate.minusMonths(1)
				Period.YEAR -> endingDate.minusYears(1)
			}
			val lastDate = DatePeriod(startingDate = startingDate, endingDate = endingDate)
			dates.add(lastDate)
		}

		return dates.reversed()
	}

	fun calculateCurrentStreak(completions: List<HabitCompletion>, dates: List<DatePeriod>): Int {
		var streak = 0

		for (period in dates.reversed()) {
			val datedCompletions = completions.filter { it.date.isIn(period) }
			println("Dated completions size is ${datedCompletions.size}")
			if (datedCompletions.sumOf { it.quantity } >= state.habit.times) {
				streak += datedCompletions.sumOf { it.quantity }
				println("sum of datedcompletions is ${datedCompletions.sumOf { it.quantity }}")
			} else {
				println("Breaking")
				break
			}
		}

		return streak
	}

	fun calculateTotalCompletions(completions: List<HabitCompletion>): Int {
		return completions.sumOf { it.quantity }
	}
}