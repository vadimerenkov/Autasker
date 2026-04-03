package vadimerenkov.autasker.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.domain.formatted
import vadimerenkov.autasker.presentation.components.context_menu.DefaultContextMenu
import vadimerenkov.autasker.presentation.components.context_menu.DeletedContextMenu
import vadimerenkov.autasker.presentation.main.MainAction
import vadimerenkov.autasker.presentation.util.ComposableDateFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun TaskItem(
	task: Task,
	onAction: (MainAction) -> Unit,
	modifier: Modifier
) {
	var subtasksOpen by remember { mutableStateOf(false) }
	var menuOpen by remember { mutableStateOf(false) }
	val secondaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

	Row(
		modifier = modifier
			.width(350.dp)
			.animateContentSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		if (task.subtasks.isNotEmpty()) {
			IconButton(
				onClick = {
					subtasksOpen = !subtasksOpen
				}
			) {
				Icon(
					imageVector = if (subtasksOpen) Icons.Default.ArrowDropDown else Icons.AutoMirrored.Filled.ArrowRight,
					tint = MaterialTheme.colorScheme.onBackground,
					contentDescription = null
				)
			}
		}
		when {
			task.isDeleted -> {
				DeletedContextMenu(
					task = task,
					isOpen = menuOpen,
					onAction = onAction,
					onDismissRequest = {
						menuOpen = false
					}
				)
			}
			else -> {
				DefaultContextMenu(
					task = task,
					onAction = {
						onAction(it)
						menuOpen = false
					},
					isOpen = menuOpen,
					onDismissRequest = {
						menuOpen = false
					}
				)
			}
		}
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.border(
					width = 1.dp,
					color = MaterialTheme.colorScheme.secondary,
					shape = RoundedCornerShape(8.dp)
				)
				.clip(
					RoundedCornerShape(8.dp)
				)
				.onClick(
					matcher = PointerMatcher.mouse(PointerButton.Secondary),
					onClick = {
						menuOpen = true
					}
				)
				.combinedClickable(
					onClick = {
						onAction(MainAction.OnTaskClick(task.id))
					}
				)
				.padding(12.dp)
				.alpha(
					alpha = if (task.isCompleted) 0.5f else 1f
				)
		) {

			if (task.dueDate != null) {
				Row(
					horizontalArrangement = Arrangement.SpaceBetween,
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						text = ComposableDateFormatter.formatDate(task.dueDate, task.isAllDay),
						style = MaterialTheme.typography.bodySmall,
						color = secondaryColor
					)
					Text(
						text = ComposableDateFormatter.formatDuration(task.dueDate, task.isAllDay),
						style = MaterialTheme.typography.bodySmall,
						color = secondaryColor
					)
				}
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
			) {
				CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
					Checkbox(
						checked = task.isCompleted,
						onCheckedChange = {
							onAction(MainAction.CheckmarkToggle(task.id, it))
						}
					)
					Spacer(modifier = Modifier.width(4.dp))
					(1..task.importance).forEach { int ->
						Text(
							text = "!",
							color = Color.Red,
							fontWeight = FontWeight.Bold
						)
					}
					Spacer(modifier = Modifier.width(4.dp))
					Text(
						text = task.title,
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onBackground
					)
				}
			}
			if (task.description != null || task.repeatState.isRepeating) {
				Row(
					horizontalArrangement = Arrangement.SpaceBetween,
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						text = task.description ?: "",
						style = MaterialTheme.typography.bodySmall,
						color = secondaryColor
					)
					if (task.repeatState.isRepeating) {
						Text(
							text = task.repeatState.formatted(),
							style = MaterialTheme.typography.bodySmall,
							color = secondaryColor
						)
					}
				}
			}
			if (task.subtasks.isNotEmpty()) {
				val progress by animateFloatAsState(task.subtasks.count { it.isCompleted } / task.subtasks.size.toFloat())
				LinearProgressIndicator(
					progress = { progress },
					drawStopIndicator = {},
					modifier = Modifier
						.fillMaxWidth()
				)
			}
			AnimatedVisibility(subtasksOpen) {

				Column {
					task.subtasks.forEachIndexed { index, subtask ->
						Row(
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								checked = subtask.isCompleted,
								onCheckedChange = {
									onAction(MainAction.SubtaskToggle(task.id, index))
								}
							)
							Text(
								text = subtask.title,
								style = MaterialTheme.typography.bodyMedium,
								color = MaterialTheme.colorScheme.onBackground
							)
						}
					}
				}
			}
		}
	}
}
