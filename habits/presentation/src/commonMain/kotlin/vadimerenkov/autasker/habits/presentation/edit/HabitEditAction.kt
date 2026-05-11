package vadimerenkov.autasker.habits.presentation.edit

sealed interface HabitEditAction {
	data class OnTitleChange(val title: String): HabitEditAction
}