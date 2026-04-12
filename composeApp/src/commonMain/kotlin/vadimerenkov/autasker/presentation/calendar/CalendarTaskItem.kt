package vadimerenkov.autasker.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.presentation.util.ComposableDateFormatter

@Composable
fun CalendarTaskItem(
	task: Task,
	onAction: (CalendarAction) -> Unit
) {
	Box(
		modifier = Modifier
			.padding(2.dp)
			.background(MaterialTheme.colorScheme.primary)
			.fillMaxWidth()
			.clickable {
				onAction(CalendarAction.OnTaskClick(task.id))
			}
	) {
		val title = if (task.dueDate == null || task.isAllDay) {
			task.title
		} else {
			val time = ComposableDateFormatter.formatTime(task.dueDate.toLocalTime())
			"$time ${task.title}"
		}
		Text(
			text = title,
			color = MaterialTheme.colorScheme.onPrimary,
			maxLines = 2,
			modifier = Modifier
				.padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
		)

	}
}