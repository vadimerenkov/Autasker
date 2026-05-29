package vadimerenkov.autasker.core.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.complete_habit
import autasker.core.presentation.generated.resources.minutes
import autasker.core.presentation.generated.resources.times_per
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitType
import vadimerenkov.autasker.core.presentation.components.ButtonsRow
import vadimerenkov.autasker.core.presentation.components.IntNumberInputField
import vadimerenkov.autasker.core.presentation.main.MainAction

@Composable
fun HabitCompletionDialog(
	habit: Habit,
	onAction: (MainAction) -> Unit,
	onDismissRequest: () -> Unit
) {
	var number: Int? by remember { mutableStateOf(1) }

	Dialog(
		onDismissRequest = onDismissRequest
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.background(MaterialTheme.colorScheme.background)
				.padding(16.dp)
		) {
			val complete = stringResource(Res.string.complete_habit)
			Text(
				text = "$complete ${habit.title}"
			)
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			) {
				IntNumberInputField(
					value = number,
					minNumber = 1,
					onValueChange = {
						number = it
					},
					modifier = Modifier
						.widthIn(max = 100.dp)
				)
				Text(
					text = when (habit.type) {
						HabitType.SINGLE -> stringResource(Res.string.times_per)
						HabitType.TIME -> stringResource(Res.string.minutes)
						HabitType.CUSTOM -> habit.customQuantifier!!
					}
				)
			}
			ButtonsRow(
				onPrimaryClick = {
					onAction(MainAction.SaveHabitCompletionClick(habit.id,number ?: 1))
					onDismissRequest()
				},
				isPrimaryButtonEnabled = number != null,
				onSecondaryClick = onDismissRequest
			)
		}
	}
}