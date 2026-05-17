package vadimerenkov.autasker.habits.presentation.edit

import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.domain.habits.HabitType

sealed interface HabitEditAction {
	data class TitleChange(val title: String): HabitEditAction
	data class TimesChange(val times: Int?): HabitEditAction
	data class PeriodChange(val period: Period): HabitEditAction
	data class TypeChange(val type: HabitType): HabitEditAction
	data class QuantifierChange(val quantifier: String): HabitEditAction
	data class AddTaskClick(val task: Task): HabitEditAction
	data class RemoveTaskClick(val task: Task): HabitEditAction
	data object OnSaveClick: HabitEditAction
}