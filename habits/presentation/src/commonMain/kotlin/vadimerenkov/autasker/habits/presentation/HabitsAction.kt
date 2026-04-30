package vadimerenkov.autasker.habits.presentation

sealed interface HabitsAction {
	data object NewHabitClick: HabitsAction
}