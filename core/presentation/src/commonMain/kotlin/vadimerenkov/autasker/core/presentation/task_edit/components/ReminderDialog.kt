package vadimerenkov.autasker.core.presentation.task_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.remind_before
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.reminders.Reminder
import vadimerenkov.autasker.core.presentation.components.ButtonsRow
import vadimerenkov.autasker.core.presentation.components.IntNumberInputField
import vadimerenkov.autasker.core.presentation.task_edit.TaskEditAction
import vadimerenkov.autasker.core.presentation.util.ComposableDateFormatter
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDialog(
	initialTime: LocalTime,
	hasTime: Boolean,
	onAction: (TaskEditAction) -> Unit,
	onDismissRequest: () -> Unit
) {
	var number by remember { mutableIntStateOf(1) }
	var period by remember { mutableStateOf(Period.DAY) }
	var menuExpanded by remember { mutableStateOf(false) }
	var timePickerDialogOpen by remember { mutableStateOf(false) }
	var time by remember { mutableStateOf(initialTime) }

	Dialog(
		properties = DialogProperties(
			usePlatformDefaultWidth = false
		),
		onDismissRequest = onDismissRequest
	) {

		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.clip(RoundedCornerShape(12.dp))
				.background(MaterialTheme.colorScheme.background)
				.padding(16.dp)
		) {
			FlowRow(
				itemVerticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(16.dp),
			) {
				Text(
					text = stringResource(Res.string.remind_before)
				)
				IntNumberInputField(
					value = number,
					minNumber = 0,
					onValueChange = {
						number = it ?: 0
					},
					modifier = Modifier
						.widthIn(max = 100.dp)
				)
				PeriodTextBoxMenu(
					isExpanded = menuExpanded,
					areHourAndMinuteEnabled = hasTime,
					period = period,
					onPeriodChange = { period = it },
					times = number,
					onExpandedChange = { menuExpanded = it },
					modifier = Modifier
						.widthIn(max = 100.dp)
				)
				if (period != Period.MINUTE && period != Period.HOUR) {
					OutlinedButton(
						onClick = { timePickerDialogOpen = true },
					) {
						Text(
							text = ComposableDateFormatter.formatTime(time)
						)
					}
				}
			}
			ButtonsRow(
				onPrimaryClick = {
					onAction(TaskEditAction.AddReminder(
						Reminder(
							period = period,
							times = number.toLong(),
							time = time
						)
					))
					onDismissRequest()
				},
				onSecondaryClick = {
					onDismissRequest()
				}
			)
		}
	}

	if (timePickerDialogOpen) {
		val initialTime = rememberTimePickerState(
			initialHour = time.hour,
			initialMinute = time.minute
		)
		TimePickerDialog(
			onDismissRequest = { timePickerDialogOpen = false },
			confirmButton = {
				Button(
					onClick = {
						val chosenTime = LocalTime.of(initialTime.hour, initialTime.minute)
						time = chosenTime
						println("Saving time as $chosenTime, time is $time")
						timePickerDialogOpen = false
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
				state = initialTime
			)
		}
	}
}