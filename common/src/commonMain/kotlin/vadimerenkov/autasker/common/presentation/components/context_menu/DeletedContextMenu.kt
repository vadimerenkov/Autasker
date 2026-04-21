package vadimerenkov.autasker.common.presentation.components.context_menu

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.delete_task_forever
import autasker.common.generated.resources.restore_task
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.presentation.main.MainAction

@Composable
fun DeletedContextMenu(
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
					text = stringResource(Res.string.delete_task_forever)
				)
			},
			onClick = {
				onAction(MainAction.DeleteTaskForever(task.id))
			}
		)
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(Res.string.restore_task)
				)
			},
			onClick = {
				onAction(MainAction.RestoreTask(task.id))
			}
		)
	}
}