package vadimerenkov.autasker.core.presentation.task_edit.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.presentation.extensions.toLocalizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodTextBoxMenu(
	period: Period?,
	onPeriodChange: (Period) -> Unit,
	times: Int,
	isExpanded: Boolean,
	onExpandedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	areHourAndMinuteEnabled: Boolean = true,
	isError: Boolean = false,
	isEnabled: Boolean = true
) {
	ExposedDropdownMenuBox(
		expanded = isExpanded,
		onExpandedChange = onExpandedChange,
		modifier = modifier
	) {

		TextField(
			value = period?.toLocalizedString(times)
				?: "",
			readOnly = true,
			enabled = isEnabled,
			onValueChange = {},
			trailingIcon = {
				if (isEnabled) {
					Icon(
						imageVector = Icons.Default.ArrowDropDown,
						contentDescription = null
					)
				}
			},
			isError = isError,
			modifier = Modifier
				.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
		)
		ExposedDropdownMenu(
			expanded = isExpanded,
			onDismissRequest = { onExpandedChange(false) }
		) {
			Period.entries.forEach { periodEntry ->
				DropdownMenuItem(
					enabled = if (periodEntry == Period.HOUR || periodEntry == Period.MINUTE) areHourAndMinuteEnabled else true,
					text = {
						Text(
							text = periodEntry.toLocalizedString(times)
						)
					},
					onClick = {
						onPeriodChange(periodEntry)
						onExpandedChange(false)
					}
				)
			}
		}
	}
}