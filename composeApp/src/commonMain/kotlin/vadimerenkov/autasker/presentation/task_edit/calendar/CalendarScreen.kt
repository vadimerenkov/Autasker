package vadimerenkov.autasker.presentation.task_edit.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.date
import autasker.composeapp.generated.resources.precisely_time
import autasker.composeapp.generated.resources.repeat
import autasker.composeapp.generated.resources.same_day
import autasker.composeapp.generated.resources.time
import autasker.composeapp.generated.resources.time_before
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import vadimerenkov.autasker.di.appModule
import vadimerenkov.autasker.di.platformModule
import vadimerenkov.autasker.domain.Period
import vadimerenkov.autasker.domain.minusReminder
import vadimerenkov.autasker.domain.reminders.Reminder
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

@Deprecated("")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
	state: DateTimeState,
	onAction: (TaskEditAction) -> Unit,
	onBackClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	var isDatePickerOpen by remember { mutableStateOf(false) }
	var isTimePickerOpen by remember { mutableStateOf(false) }
	var isReminderDialogOpen by remember { mutableStateOf(false) }
	val repeat = stringResource(Res.string.repeat)
	var height by remember { mutableStateOf(0.dp) }
	val density = LocalDensity.current

	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
			.fillMaxSize()

	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.onSizeChanged {
					with(density) {
						height = it.height.toDp()
					}
				}
		) {
			CalendarRow(
				leftColumn = {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							text = stringResource(Res.string.date),
							color = MaterialTheme.colorScheme.onBackground
						)
					}
				},
				rightColumn = {
					OutlinedButton(
						onClick = {
							isDatePickerOpen = true
						}
					) {
						Text(
							text = ComposableDateFormatter.formatDate(state.dateTime, !state.hasTime),
							fontSize = 24.sp
						)
					}
				}
			)
			HorizontalDivider()
			CalendarRow(
				leftColumn = {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Checkbox(
							checked = state.hasTime,
							onCheckedChange = {
								onAction(TaskEditAction.HasTimeToggle(it))
							}
						)
						Text(
							text = stringResource(Res.string.time),
							color = MaterialTheme.colorScheme.onBackground,
						)
					}
				},
				rightColumn = {
					OutlinedButton(
						enabled = state.hasTime,
						onClick = {
							isTimePickerOpen = true
						}
					) {
						Text(
							text = ComposableDateFormatter.formatTime(state.time)
						)
					}
				}
			)
			HorizontalDivider()

			CalendarRow(
				leftColumn = {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Checkbox(
							checked = state.isRepeated,
							onCheckedChange = {
								onAction(TaskEditAction.RepeatToggle(it))
							}
						)
						Text(
							text = repeat,
							color = MaterialTheme.colorScheme.onBackground,
							modifier = Modifier
								.clickable {
									onAction(TaskEditAction.RepeatToggle(!state.isRepeated))
								}
						)
					}
				},
				rightColumn = {
					RepeatMenu(
						state = state,
						onAction = onAction
					)
				}
			)
			HorizontalDivider()

			CalendarRow(
				leftColumn = {
					Text(
						text = "Reminders"
					)
				},
				rightColumn = {
					state.reminders.forEach { reminder ->
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.spacedBy(8.dp)
						) {
							val times = reminder.times.toInt()
							val period = reminder.period.toLocalizedString(times)
							val date = state.dateTime.minusReminder(reminder)
							DataChip(
								text = when (times) {
									0 if date.roundToMinutes() == state.dateTime.roundToMinutes() -> {
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
								text = ComposableDateFormatter.formatDate(state.dateTime.minusReminder(reminder), false)
							)
						}
					}
					Button(
						onClick = {
							isReminderDialogOpen = true
						}
					) {
						Text(
							text = "+ Add reminder"
						)
					}
				}
			)
		}
		Spacer(modifier = Modifier.weight(1f))
		ButtonsRow(
			isPrimaryButtonEnabled = state.isValid,
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

	Row(
		modifier = Modifier
			.fillMaxSize()
	) {
		Box(
			modifier = Modifier
				.weight(1f)
		)
		VerticalDivider(
			modifier = Modifier
				.height(height)
		)
		Box(
			modifier = Modifier
				.weight(3f)
		)
	}

	if (isReminderDialogOpen) {
		ReminderDialog(
			onAction = onAction,
			initialTime = state.time,
			hasTime = state.hasTime,
			onDismissRequest = { isReminderDialogOpen = false }
		)
	}

	if (isDatePickerOpen) {
		val date = rememberDatePickerState(
			initialSelectedDateMillis = state.dateTime.toEpochSecond().times(1000L)
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
					Text(text = "confirm")
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
			initialHour = state.time.hour,
			initialMinute = state.time.minute
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
					Text(text = "confirm")
				}
			},
			title = {
				Text("Choose time")
			}
		) {
			TimePicker(
				state = time
			)
		}
	}
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun CalendarPreview() {
	KoinApplicationPreview(application = {modules(appModule, platformModule)}) {
		AutaskerTheme {
			CalendarScreen(
				state = DateTimeState(
					isRepeated = true,
					period = Period.WEEK,
					reminders = listOf(
						Reminder()
					)
				),
				onAction = {},
				onBackClick = {}
			)
		}
	}
}