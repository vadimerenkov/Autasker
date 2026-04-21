package vadimerenkov.autasker.common.presentation.new_day

sealed interface NewDayAction {
	data object NextButtonClick: NewDayAction
	data object PreviousButtonClick: NewDayAction
	data class TaskClick(val id: Long): NewDayAction
	data class SetTodayClick(val id: Long, val isSet: Boolean): NewDayAction
}