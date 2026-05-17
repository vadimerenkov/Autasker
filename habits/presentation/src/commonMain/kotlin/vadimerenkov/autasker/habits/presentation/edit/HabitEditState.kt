package vadimerenkov.autasker.habits.presentation.edit

import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.domain.habits.HabitType

data class HabitEditState(
	val title: String = "",
	val times: Int? = 1,
	val period: Period = Period.DAY,
	val type: HabitType = HabitType.SINGLE,
	val customQuantifier: String = "",
	val tasks: List<Task> = emptyList(),
	val allTasks: List<Task> = emptyList()
) {
	val isValid
		get() = (title.isNotBlank() && times != null) && !(type == HabitType.CUSTOM && customQuantifier.isBlank())
}