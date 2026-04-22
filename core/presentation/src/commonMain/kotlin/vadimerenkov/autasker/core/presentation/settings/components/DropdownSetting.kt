package vadimerenkov.autasker.core.presentation.settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropdownSetting(
	options: List<String>,
	description: String,
	chosenOption: String,
	onOptionChosen: (Int) -> Unit,
	modifier: Modifier = Modifier,
	icon: DrawableResource? = null
) {
	var expanded by remember { mutableStateOf(false) }

	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
			.fillMaxWidth()
	) {
		Row(
			modifier = Modifier
				.weight(2f)
		) {
			icon?.let {
				Icon(
					imageVector = vectorResource(icon),
					contentDescription = null,
					tint = MaterialTheme.colorScheme.primary
				)
				Spacer(modifier = Modifier.width(8.dp))
			}
			Text(
				text = description,
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onBackground
			)
		}
		ExposedDropdownMenuBox(
			expanded = expanded,
			onExpandedChange = { expanded = it },
			modifier = Modifier
				.weight(3f)
		) {
			TextField(
				value = chosenOption,
				onValueChange = {},
				readOnly = true,
				modifier = Modifier.Companion.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
				trailingIcon = { Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null) }
			)
			val scroll = rememberScrollState()
			ExposedDropdownMenu(
				expanded = expanded,
				scrollState = scroll,
				onDismissRequest = { expanded = !expanded },
			) {
				options.forEachIndexed { index, option ->

					DropdownMenuItem(
						text = { Text(option) },
						trailingIcon = {
							if (option == chosenOption) {
								Icon(
									imageVector = Icons.Default.Check,
									contentDescription = null
								)
							}
						},
						onClick = {
							onOptionChosen(index)
							expanded = false
						},
						modifier = Modifier
							.padding(end = 6.dp)
					)
				}
			}
		}
	}
}