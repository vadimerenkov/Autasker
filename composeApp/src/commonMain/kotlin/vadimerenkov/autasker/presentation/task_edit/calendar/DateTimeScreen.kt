package vadimerenkov.autasker.presentation.task_edit.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.add_reminder
import autasker.composeapp.generated.resources.choose_time
import autasker.composeapp.generated.resources.date
import autasker.composeapp.generated.resources.precisely_time
import autasker.composeapp.generated.resources.reminders
import autasker.composeapp.generated.resources.repeat
import autasker.composeapp.generated.resources.same_day
import autasker.composeapp.generated.resources.save
import autasker.composeapp.generated.resources.time
import autasker.composeapp.generated.resources.time_before
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import vadimerenkov.autasker.di.appModule
import vadimerenkov.autasker.di.platformModule
import vadimerenkov.autasker.domain.minusReminder
import vadimerenkov.autasker.domain.roundToMinutes
import vadimerenkov.autasker.domain.toLocalizedString
import vadimerenkov.autasker.presentation.components.ButtonsRow
import vadimerenkov.autasker.presentation.components.DataChip
import vadimerenkov.autasker.presentation.task_edit.TaskEditAction
import vadimerenkov.autasker.presentation.task_edit.components.ReminderDialog
import vadimerenkov.autasker.presentation.task_edit.components.RepeatMenu
import vadimerenkov.autasker.presentation.theme.AutaskerTheme
import vadimerenkov.autasker.presentation.util.ComposableDateFormatter
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeScreen(
	dateTimeState: DateTimeState,
	onAction: (TaskEditAction) -> Unit,
	onBackClick: () -> Unit,
	modifier: Modifier = Modifier,
	scrollState: ScrollState = rememberScrollState()
) {
	var isDatePickerOpen by remember { mutableStateOf(false) }
	var isTimePickerOpen by remember { mutableStateOf(false) }
	var isReminderDialogOpen by remember { mutableStateOf(false) }

	Column(
		modifier = modifier
			.verticalScroll(scrollState)
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(6.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Checkbox(
				checked = dateTimeState.hasDate,
				onCheckedChange = {
					onAction(TaskEditAction.HasDateToggle(it))
				}
			)
			Text(
				text = stringResource(Res.string.date)
			)
			HorizontalDivider(
				thickness = 2.dp,
				modifier = Modifier
					.weight(1f)
			)
		}
		AnimatedVisibility(visible = dateTimeState.hasDate) {
			OutlinedButton(
				onClick = { isDatePickerOpen = true }
			) {
				Text(
					text = ComposableDateFormatter.formatDate(dateTimeState.dateTime, !dateTimeState.hasTime)
				)
			}
		}
		Row(
			horizontalArrangement = Arrangement.spacedBy(6.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Checkbox(
				checked = dateTimeState.hasTime,
				onCheckedChange = {
					onAction(TaskEditAction.HasTimeToggle(it))
				},
				enabled = dateTimeState.hasDate
			)
			Text(
				text = stringResource(Res.string.time)
			)
			HorizontalDivider(
				thickness = 2.dp,
				modifier = Modifier
					.weight(1f)
			)
		}
		AnimatedVisibility(visible = dateTimeState.hasTime){
			OutlinedButton(
				onClick = { isTimePickerOpen = true }
			) {
				Text(
					text = ComposableDateFormatter.formatTime(dateTimeState.time)
				)
			}
		}
		Row(
			horizontalArrangement = Arrangement.spacedBy(6.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Checkbox(
				enabled = dateTimeState.hasDate,
				checked = dateTimeState.isRepeated,
				onCheckedChange = {
					onAction(TaskEditAction.RepeatToggle(it))
				}
			)
			Text(
				text = stringResource(Res.string.repeat)
			)
			HorizontalDivider(
				thickness = 2.dp,
				modifier = Modifier
					.weight(1f)
			)
		}
		AnimatedVisibility(visible = dateTimeState.isRepeated) {
			RepeatMenu(
				state = dateTimeState,
				onAction = onAction
			)
		}
		Row(
			horizontalArrangement = Arrangement.spacedBy(6.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			HorizontalDivider(
				thickness = 2.dp,
				modifier = Modifier
					.width(16.dp)
			)
			Text(
				text = stringResource(Res.string.reminders)
			)
			HorizontalDivider(
				thickness = 2.dp,
				modifier = Modifier
					.weight(1f)
			)
		}
		Spacer(modifier = Modifier.height(8.dp))
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			dateTimeState.reminders.forEach { reminder ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					val times = reminder.times.toInt()
					val period = reminder.period.toLocalizedString(times)
					val date = dateTimeState.dateTime.minusReminder(reminder)
					DataChip(
						text = when (times) {
							0 if date.roundToMinutes() == dateTimeState.dateTime.roundToMinutes() -> {
								stringResource(Res.string.precisely_time)
							}
							0 -> {
								stringResource(Res.string.same_day)
							}
							else -> {
								stringResource(Res.string.time_before, times, period)
							}
						},
						onCrossClick = {
							onAction(TaskEditAction.RemoveReminder(reminder))
						}
					)
					Text(
						text = ComposableDateFormatter.formatDate(dateTimeState.dateTime.minusReminder(reminder), false)
					)
				}
			}

		}
		Spacer(modifier = Modifier.height(8.dp))
		Button(
			enabled = dateTimeState.hasDate,
			onClick = {
				isReminderDialogOpen = true
			}
		) {
			Text(
				text = stringResource(Res.string.add_reminder)
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		ButtonsRow(
			isPrimaryButtonEnabled = dateTimeState.isValid,
			onPrimaryClick = {
				onAction(TaskEditAction.ConfirmCalendarChanges)
				onBackClick()
			},
			onSecondaryClick = {
				onAction(TaskEditAction.CalendarToggle)
				onBackClick()
			}
		)
	}

	if (isReminderDialogOpen) {
		ReminderDialog(
			onAction = onAction,
			initialTime = dateTimeState.time,
			hasTime = dateTimeState.hasTime,
			onDismissRequest = { isReminderDialogOpen = false }
		)
	}

	if (isDatePickerOpen) {
		val date = rememberDatePickerState(
			initialSelectedDateMillis = dateTimeState.dateTime.toEpochSecond().times(1000L)
		)
		DatePickerDialog(
			onDismissRequest = { isDatePickerOpen = false },
			confirmButton = {
				Button(
					onClick = {
						date.selectedDateMillis?.let {
							val i = Instant.ofEpochMilli(it)
							val z = ZonedDateTime.ofInstant(i, ZoneId.systemDefault())
							val d = z.toLocalDate()
							onAction(TaskEditAction.DateChange(d))
						}
						isDatePickerOpen = false
					}
				) {
					Text(text = stringResource(Res.string.save))
				}
			}
		) {
			DatePicker(
				state = date
			)
		}
	}

	if (isTimePickerOpen) {
		val time = rememberTimePickerState(
			initialHour = dateTimeState.time.hour,
			initialMinute = dateTimeState.time.minute
		)
		TimePickerDialog(
			onDismissRequest = { isTimePickerOpen = false },
			confirmButton = {
				Button(
					onClick = {
						val t = LocalTime.of(time.hour, time.minute)
						onAction(TaskEditAction.TimeChange(t))
						isTimePickerOpen = false
					}
				) {
					Text(text = stringResource(Res.string.save))
				}
			},
			title = {
				Text(text = stringResource(Res.string.choose_time))
			}
		) {
			TimePicker(
				state = time
			)
		}
	}
}

@Composable
@Preview
private fun DateTimeScreenPreview() {
	KoinApplicationPreview(
		application = {
			modules(appModule, platformModule)
		}
	) {
		AutaskerTheme {
			DateTimeScreen(
				dateTimeState = DateTimeState(),
				onAction = {},
				onBackClick = {}
			)
		}
	}
}