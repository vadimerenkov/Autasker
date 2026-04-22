package vadimerenkov.autasker.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.presentation.components.context_menu.DefaultContextMenu
import vadimerenkov.autasker.core.presentation.components.context_menu.DeletedContextMenu
import vadimerenkov.autasker.core.presentation.extensions.formatted
import vadimerenkov.autasker.core.presentation.main.MainAction
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme
import vadimerenkov.autasker.core.presentation.util.ComposableDateFormatter

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
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
//			.fillMaxWidth()
			.animateContentSize()
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
					onAction = { onAction },
					onDismissRequest = {
						menuOpen = false
					}
				)
			}
			else -> {
				DefaultContextMenu(
					task = task,
					onAction = onAction,
					isOpen = menuOpen,
					onDismissRequest = {
						menuOpen = false
					}
				)
			}
		}


		var height by remember { mutableStateOf(0.dp) }
		Row(
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.border(
					width = 1.dp,
					color = if (!task.isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
					shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp)
				)
				.clip(
					RoundedCornerShape(8.dp)
				)
				.clickable {
					menuOpen = true
				}
//				.combinedClickable(
//					onClick = {
//						onAction(TaskAction.OnClick(task.id))
//					},
//					onLongClick = {
//						menuOpen = true
//					}
//				)
				.background(MaterialTheme.colorScheme.background)
				.padding(12.dp)
				.alpha(
					alpha = if (task.isCompleted) 0.5f else 1f
				)

		) {
			val density = LocalDensity.current
			Box(
				modifier = Modifier
					.clickable {
						onAction(MainAction.CheckmarkToggle(task.id, !task.isCompleted))
					}
					.padding(4.dp)
					.onSizeChanged { size ->
						with (density) {
							height = size.height.toDp()
						}
					}
			) {
				Checkbox(
					checked = task.isCompleted,
					onCheckedChange = {
						onAction(MainAction.CheckmarkToggle(task.id, it))
					}
				)
			}
			VerticalDivider(modifier = Modifier.height(height))
			Column(
				verticalArrangement = Arrangement.spacedBy(6.dp),
				modifier = Modifier
//					.height(height)
			) {

				if (task.dueDate != null) {
					Row(
						horizontalArrangement = Arrangement.SpaceBetween,
						modifier = Modifier
							.fillMaxWidth()
					) {
						Text(
							text = ComposableDateFormatter.formatDate(task.dueDate!!, task.isAllDay),
							style = MaterialTheme.typography.bodySmall,
							color = secondaryColor
						)
						Text(
							text = ComposableDateFormatter.formatDuration(task.dueDate!!, task.isAllDay),
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
					if (task.importance > 0) {
						(1..task.importance).forEach { int ->
							Text(
								text = "!",
								color = Color.Red,
								fontWeight = FontWeight.Bold
							)
						}
						Spacer(modifier = Modifier.width(4.dp))
					}
					Text(
						text = task.title,
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onBackground
					)
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
}

@Composable
@Preview
private fun TaskItemPreview() {
	AutaskerTheme {
		TaskItem(
			task = Task(),
			onAction = {}
		)
	}
}