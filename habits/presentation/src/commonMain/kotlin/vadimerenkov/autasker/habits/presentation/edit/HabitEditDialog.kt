package vadimerenkov.autasker.habits.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vadimerenkov.autasker.core.presentation.components.IntNumberInputField
import vadimerenkov.autasker.core.presentation.task_edit.components.PeriodTextBoxMenu
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme

@Composable
fun HabitEditDialog(
	state: HabitEditState,
	onAction: (HabitEditAction) -> Unit
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.clip(RoundedCornerShape(12.dp))
			.background(MaterialTheme.colorScheme.background)
			.padding(16.dp)
	) {
		OutlinedTextField(
			value = state.title,
			onValueChange = {
				onAction(HabitEditAction.OnTitleChange(it))
			}
		)
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(16.dp)
		) {
			IntNumberInputField(
				value = state.times,
				onValueChange = {

				},
				modifier = Modifier
					.widthIn(max = 150.dp)
			)
			Text(
				text = "times per"
			)
			PeriodTextBoxMenu(
				times = state.times ?: 0,
				period = state.period,
				onPeriodChange = {},
				onExpandedChange = {},
				areHourAndMinuteEnabled = false
			)
		}
	}
}

@Preview
@Composable
private fun HabitEditDialogPreview() {
	AutaskerTheme {
		HabitEditDialog(
			state = HabitEditState(),
			onAction = {}
		)
	}
}