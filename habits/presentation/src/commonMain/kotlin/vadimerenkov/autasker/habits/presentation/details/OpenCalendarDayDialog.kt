package vadimerenkov.autasker.habits.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.total_completions
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import vadimerenkov.autasker.core.presentation.components.ButtonsRow
import vadimerenkov.autasker.core.presentation.components.IntNumberInputField
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun OpenCalendarDayDialog(
	day: LocalDate,
	state: HabitDetailsState,
	onAction: (HabitDetailsAction) -> Unit
) {
	Dialog(
		onDismissRequest = {
			onAction(HabitDetailsAction.DayDialogDismiss)
		}
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.background(MaterialTheme.colorScheme.background)
				.padding(16.dp)
		) {
			Text(
				text = day.toString()
			)
			val dailyCompletions = remember { state.completions.filter { it.date.toLocalDate() == day }.toMutableStateList() }
			val totalQuantity = dailyCompletions.sumOf { it.quantity }
			val completionsToDelete = remember { mutableListOf<HabitCompletion>() }
			val total = stringResource(Res.string.total_completions)
			Text(
				text = "$total $totalQuantity"
			)
			dailyCompletions.forEachIndexed { index, completion ->
				Row {
					IntNumberInputField(
						value = completion.quantity,
						minNumber = 1,
						onValueChange = { quantity ->
							dailyCompletions.removeAt(index)
							dailyCompletions.add(index, completion.copy(quantity = quantity ?: 0))
						},
						modifier = Modifier
							.widthIn(max = 100.dp)
					)
					IconButton(
						onClick = {
							if (completion.id != 0L) {
								completionsToDelete.add(completion)
							}
							dailyCompletions.remove(completion)
						}
					) {
						Icon(
							imageVector = Icons.Default.Delete,
							contentDescription = null
						)
					}
				}
			}
			IconButton(
				onClick = {
					dailyCompletions.add(
						HabitCompletion(
							habitId = state.habit.id,
							date = day.atStartOfDay(ZoneId.systemDefault()),
							quantity = 1
						)
					)
				}
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = null
				)
			}
			ButtonsRow(
				onPrimaryClick = {
					onAction(HabitDetailsAction.DayDialogSave(dailyCompletions.toList()))
					onAction(HabitDetailsAction.DeleteCompletions(completionsToDelete))
					onAction(HabitDetailsAction.DayDialogDismiss)
				},
				onSecondaryClick = {
					onAction(HabitDetailsAction.DayDialogDismiss)
				}
			)
		}
	}
}

@Composable
@Preview
private fun DialogPreview() {
	AutaskerTheme {
		OpenCalendarDayDialog(
			day = LocalDate.now(),
			state = HabitDetailsState(),
			onAction = {}
		)
	}
}