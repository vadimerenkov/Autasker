package vadimerenkov.autasker.core.presentation.components.context_menu

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.clear_date
import autasker.core.presentation.generated.resources.delete_task
import autasker.core.presentation.generated.resources.edit_task
import autasker.core.presentation.generated.resources.move_to
import autasker.core.presentation.generated.resources.set_for_today
import autasker.core.presentation.generated.resources.set_for_tomorrow
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.presentation.main.MainAction

@Composable
fun DefaultContextMenu(
	task: Task,
	onAction: (MainAction) -> Unit,
	onDismissRequest: () -> Unit,
	modifier: Modifier = Modifier,
	isOpen: Boolean = false
) {
	DropdownMenu(
		expanded = isOpen,
		onDismissRequest = onDismissRequest,
		modifier = modifier
	) {
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.edit_task)
				)
			},
			onClick = {
				onAction(MainAction.OnTaskClick(task.id))
			}
		)
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.set_for_today)
				)
			},
			onClick = {
				onAction(MainAction.SetForToday(task.id))
			}
		)
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.set_for_tomorrow)
				)
			},
			onClick = {
				onAction(MainAction.SetForTomorrow(task.id))
			}
		)
		DropdownMenuItem(
			enabled = task.dueDate != null,
			text = {
				Text(
					text = stringResource(Res.string.clear_date)
				)
			},
			onClick = {
				onAction(MainAction.ClearDate(task.id))
			}
		)
		HorizontalDivider()
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.move_to)
				)
			},
			onClick = {
				onAction(MainAction.MoveTaskClick(task.id))
			}
		)
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.delete_task)
				)
			},
			onClick = {
				onAction(MainAction.DeleteTask(task.id))
			}
		)
	}
}