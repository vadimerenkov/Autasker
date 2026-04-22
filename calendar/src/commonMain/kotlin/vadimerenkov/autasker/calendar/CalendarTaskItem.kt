package vadimerenkov.autasker.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.presentation.util.ComposableDateFormatter

@Composable
fun CalendarTaskItem(
	task: Task,
	onAction: (CalendarAction) -> Unit
) {
	val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
	val fontSize = if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
		14.sp
	} else {
		10.sp
	}
	val padding = if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
		4.dp
	} else {
		2.dp
	}
	Box(
		modifier = Modifier
			.padding(vertical = 2.dp)
			.background(MaterialTheme.colorScheme.primary)
			.fillMaxWidth()
			.clickable {
				onAction(CalendarAction.OnTaskClick(task.id))
			}
	) {
		val title = if (task.dueDate == null || task.isAllDay) {
			task.title
		} else {
			val time = ComposableDateFormatter.formatTime(task.dueDate!!.toLocalTime())
			"$time ${task.title}"
		}
		Text(
			text = title,
			color = MaterialTheme.colorScheme.onPrimary,
			fontSize = fontSize,
			maxLines = 2,
			modifier = Modifier
				.padding(horizontal = 4.dp)
		)

	}
}