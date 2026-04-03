package vadimerenkov.autasker.presentation.task_edit.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import vadimerenkov.autasker.domain.Period
import vadimerenkov.autasker.domain.RepeatMode
import vadimerenkov.autasker.domain.getWordEvery
import vadimerenkov.autasker.domain.toLocalizedString
import vadimerenkov.autasker.presentation.components.IntNumberInputField
import vadimerenkov.autasker.presentation.task_edit.TaskEditAction
import vadimerenkov.autasker.presentation.task_edit.calendar.DateTimeState
import vadimerenkov.autasker.settings.Settings
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatMenu(
	state: DateTimeState,
	onAction: (TaskEditAction) -> Unit
) {
	val every = state.period?.getWordEvery(state.times?.toInt() ?: 0 )
	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
		) {
			Text(
				text = every ?: Period.DAY.getWordEvery(state.times?.toInt() ?: 0),
				color = MaterialTheme.colorScheme.onBackground
			)
			IntNumberInputField(
				value = state.times?.toInt(),
				minNumber = 1,
				onValueChange = {
					onAction(TaskEditAction.TimesChange(it?.toLong()))
				},
				enabled = state.isRepeated,
				isError = !state.isTimesValueValid,
				modifier = Modifier
					.widthIn(max = 100.dp)
			)
			Spacer(modifier = Modifier.width(8.dp))

			var periodOpen by remember { mutableStateOf(false) }
			PeriodTextBoxMenu(
				period = state.period,
				onPeriodChange = {
					onAction(TaskEditAction.PeriodChange(it))
					periodOpen = false
				},
				isExpanded = periodOpen,
				onExpandedChange = {
					if (state.isRepeated) {
						periodOpen = it
					}
				},
				times = state.times?.toInt() ?: 0,
				areHourAndMinuteEnabled = state.hasTime,
				isError = !state.isPeriodValueValid,
				isEnabled = state.isRepeated
			)
		}
		AnimatedVisibility(state.period == Period.WEEK && state.isRepeated) {
			Row(
				horizontalArrangement = Arrangement.SpaceAround,
				modifier = Modifier
					.fillMaxWidth()
			) {
				val settings = koinInject<Settings>()
				val o = settings.state.firstDayOfWeek.ordinal
				val firstPart = DayOfWeek.entries.subList(
					fromIndex = o,
					toIndex = DayOfWeek.entries.lastIndex + 1
				)
				val secondPart = DayOfWeek.entries - firstPart
				val days = firstPart + secondPart
				days.forEach { weekday ->
					val selected = state.weekdays.contains(weekday)
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.size(48.dp)
							.clip(RoundedCornerShape(8.dp))
							.border(
								border = BorderStroke(2.dp, color = Color.Gray),
								shape = RoundedCornerShape(8.dp)
							)
							.background(color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
							.clickable {
								onAction(TaskEditAction.WeekdaySelected(weekday))
							}
							.padding(8.dp)
					) {
						Text(
							text = weekday.getDisplayName(
								TextStyle.SHORT,
								Locale.getDefault()
							)
						)
					}
				}

			}
		}
		AnimatedVisibility(state.isRepeated) {
			Column(
				verticalArrangement = Arrangement.spacedBy(12.dp)
			) {
				RepeatMode.entries.forEach { mode ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
//							.fillMaxWidth()
							.clickable {
								onAction(TaskEditAction.RepeatModeChange(mode))
							}
					) {
						RadioButton(
							selected = state.repeatMode == mode,
							onClick = {
								onAction(TaskEditAction.RepeatModeChange(mode))
							}
						)
						Spacer(modifier = Modifier.width(4.dp))
						Column {
							val interval =
								"${state.times} ${state.period?.toLocalizedString(state.times?.toInt() ?: 0)}"
							Text(
								text = stringResource(mode.title),
								style = MaterialTheme.typography.displaySmall,
								color = MaterialTheme.colorScheme.onBackground
							)
							Text(
								text = stringResource(
									mode.description,
									interval.lowercase()
								),
								style = MaterialTheme.typography.bodySmall,
								color = MaterialTheme.colorScheme.secondary
							)
						}
					}
				}
			}
		}
	}
}