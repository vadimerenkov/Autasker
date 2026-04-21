package vadimerenkov.autasker.common.presentation.task_edit

import vadimerenkov.autasker.common.domain.Period
import vadimerenkov.autasker.common.domain.RepeatMode
import vadimerenkov.autasker.common.domain.reminders.Reminder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskEditAction {
	data class TitleChange(val title: String): TaskEditAction
	data class DescriptionChange(val description: String): TaskEditAction
	data class DateChange(val date: LocalDate): TaskEditAction
	data class TimeChange(val time: LocalTime): TaskEditAction
	data class RepeatToggle(val repeat: Boolean): TaskEditAction
	data class RepeatModeChange(val mode: RepeatMode): TaskEditAction
	data class TimesChange(val times: Long?): TaskEditAction
	data class PeriodChange(val period: Period): TaskEditAction
	data class CompletedToggle(val isCompleted: Boolean): TaskEditAction
	data class HasDateToggle(val hasDate: Boolean): TaskEditAction
	data class HasTimeToggle(val hasTime: Boolean): TaskEditAction
	data object ConfirmCalendarChanges: TaskEditAction
	data object CalendarToggle: TaskEditAction
	data object DeleteDate: TaskEditAction
	data object DeleteRepeating: TaskEditAction
	data class SaveSubtask(val title: String): TaskEditAction
	data class SubtaskToggle(val index: Int): TaskEditAction
	data class DeleteSubtask(val index: Int): TaskEditAction
	data class ImportanceChanged(val int: Int): TaskEditAction
	data class CategorySelected(val id: Long): TaskEditAction
	data class CommonDatePicked(val date: Int): TaskEditAction
	data class WeekdaySelected(val weekday: DayOfWeek): TaskEditAction
	data class AddReminder(val reminder: Reminder): TaskEditAction
	data class RemoveReminder(val reminder: Reminder): TaskEditAction
}