package vadimerenkov.autasker.common.presentation.components.context_menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.delete_column
import autasker.common.generated.resources.move_to
import autasker.common.generated.resources.set_default
import autasker.common.generated.resources.sort_by
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.common.domain.Sorting
import vadimerenkov.autasker.common.domain.TaskCategory
import vadimerenkov.autasker.common.domain.toUiText
import vadimerenkov.autasker.common.presentation.main.MainAction

@Composable
fun ColumnContextMenu(
	category: TaskCategory,
	onTaskAction: (MainAction) -> Unit,
	isMenuOpen: Boolean = false,
	onDismissRequest: () -> Unit
) {
	DropdownMenu(
		expanded = isMenuOpen,
		onDismissRequest = onDismissRequest
	) {
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.set_default)
				)
			},
			enabled = !category.isDefault && category.isEditable,
			onClick = {
				onTaskAction(MainAction.SetColumnDefault(category.id))
				onDismissRequest()
			}
		)
		var sortingSubmenuOpen by remember { mutableStateOf(false) }
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.sort_by)
				)
				DropdownMenu(
					expanded = sortingSubmenuOpen,
					onDismissRequest = { sortingSubmenuOpen = false }
				) {
					Sorting.entries.forEach {
						DropdownMenuItem(
							text = {
								Text(
									text = it.toUiText()
								)
							},
							trailingIcon = {
								if (category.sorting == it) {
									Icon(
										imageVector = Icons.Default.Check,
										contentDescription = null
									)
								}
							},
							onClick = {
								onTaskAction(MainAction.ChangeColumnSorting(category.id, it))
							}
						)
					}
				}
			},
			trailingIcon = {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowRight,
					contentDescription = null
				)
			},
			onClick = { sortingSubmenuOpen = true }
		)
		DropdownMenuItem(
			enabled = category.canDelete,
			text = {
				Text(
					text = stringResource(Res.string.move_to)
				)
			},
			onClick = {
				onTaskAction(MainAction.MoveColumnClick(category.id))
				onDismissRequest()
			}
		)
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.delete_column)
				)
			},
			enabled = category.canDelete,
			onClick = {
				onTaskAction(MainAction.DeleteColumn(category.id))
				onDismissRequest()
			}
		)
	}
}