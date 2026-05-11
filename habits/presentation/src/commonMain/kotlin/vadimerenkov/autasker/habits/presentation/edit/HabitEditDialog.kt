package vadimerenkov.autasker.habits.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vadimerenkov.autasker.core.domain.habits.HabitType
import vadimerenkov.autasker.core.presentation.components.ButtonsRow
import vadimerenkov.autasker.core.presentation.components.IntNumberInputField
import vadimerenkov.autasker.core.presentation.task_edit.components.PeriodTextBoxMenu
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme
import vadimerenkov.autasker.habits.presentation.toLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitEditDialog(
	state: HabitEditState,
	onAction: (HabitEditAction) -> Unit,
	onDismissRequest: () -> Unit
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.clip(RoundedCornerShape(12.dp))
			.background(MaterialTheme.colorScheme.background)
			.padding(16.dp)
	) {
		OutlinedTextField(
			value = state.title,
			onValueChange = {
				onAction(HabitEditAction.TitleChange(it))
			},
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
		)
		var expanded by remember { mutableStateOf(false) }
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Count: "
			)
			ExposedDropdownMenuBox(
				expanded = expanded,
				onExpandedChange = {
					expanded = it
				}
			) {
				TextField(
					value = state.type.toLocalizedString(),
					readOnly = true,
					onValueChange = {},
					trailingIcon = {
						Icon(
							imageVector = Icons.Default.ArrowDropDown,
							contentDescription = null
						)
					},
					modifier = Modifier
						.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
				)
				ExposedDropdownMenu(
					expanded = expanded,
					onDismissRequest = { expanded = false }
				) {
					HabitType.entries.forEach { type ->
						DropdownMenuItem(
							text = {
								Text(
									text = type.toLocalizedString()
								)
							},
							onClick = {
								onAction(HabitEditAction.TypeChange(type))
								expanded = false
							}
						)
					}
				}
			}
		}
		if (state.type == HabitType.CUSTOM) {
			OutlinedTextField(
				value = state.customQuantifier,
				onValueChange = {
					onAction(HabitEditAction.QuantifierChange(it))
				}
			)
		}
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(16.dp)
		) {
			IntNumberInputField(
				value = state.times,
				minNumber = 1,
				isError = state.times == null,
				onValueChange = {
					onAction(HabitEditAction.TimesChange(it))
				},
				modifier = Modifier
					.widthIn(max = 150.dp)
			)
			Text(
				text = "times per"
			)
			var expanded by remember { mutableStateOf(false) }

			PeriodTextBoxMenu(
				times = 1,
				period = state.period,
				onPeriodChange = {
					onAction(HabitEditAction.PeriodChange(it))
					expanded = false
				},
				isExpanded = expanded,
				onExpandedChange = { expanded = it},
				areHourAndMinuteEnabled = false
			)
		}


		ButtonsRow(
			onPrimaryClick = {
				onAction(HabitEditAction.OnSaveClick)
				onDismissRequest()
			},
			isPrimaryButtonEnabled = state.isValid,
			onSecondaryClick = onDismissRequest
		)
	}
}

@Preview
@Composable
private fun HabitEditDialogPreview() {
	AutaskerTheme {
		HabitEditDialog(
			state = HabitEditState(
				type = HabitType.CUSTOM
			),
			onAction = {},
			onDismissRequest = {}
		)
	}
}