package vadimerenkov.autasker.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.presentation.theme.AutaskerTheme
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CalendarDayDialog(
	selectedDay: LocalDate,
	tasks: List<Task>,
	onDismissRequest: () -> Unit
) {
	val allDayTasks = tasks.filter { it.isAllDay }
	val timedTasks = tasks - allDayTasks.toSet()
	val tasksAtHour = timedTasks.associateBy { it.dueDate!!.hour }
	Dialog(
		onDismissRequest = onDismissRequest
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.background(MaterialTheme.colorScheme.background)
				.padding(16.dp)
		) {
			Text(
				text = selectedDay.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
				fontSize = 20.sp
			)
			LazyColumn(
//			    verticalArrangement = Arrangement.spacedBy(16.dp),
			) {
				item {
					Column(
						verticalArrangement = Arrangement.spacedBy(2.dp)
					) {
						allDayTasks.forEach { task ->
							Box(
								modifier = Modifier
									.fillMaxWidth()
									.background(MaterialTheme.colorScheme.primary)
							) {
								Text(
									text = task.title,
									color = MaterialTheme.colorScheme.onPrimary,
									modifier = Modifier.padding(4.dp)
								)
							}
						}
					}
				}
				(0..23).forEach { hour ->
					item {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.fillMaxWidth()
						) {
							Text(
								text = hour.toString()
							)
							Column {
								HorizontalDivider()
								if (tasksAtHour.containsKey(hour)) {
									val task = tasksAtHour[hour]!!
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.background(MaterialTheme.colorScheme.primary)
									) {
										Text(
											text = task.title,
											color = MaterialTheme.colorScheme.onPrimary,
											modifier = Modifier.padding(4.dp)
										)
									}
								}
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
private fun CalendarDayDialogPreview() {
	AutaskerTheme {
		CalendarDayDialog(
			tasks = listOf(
				Task(
					title = "Test task"
				),
				Task(
					title = "Test task 2"
				),
				Task(
					title = "Task with date",
					dueDate = ZonedDateTime.now(),
					isAllDay = false
				),
				Task(
					title = "Task with another date",
					dueDate = ZonedDateTime.now().minusHours(5),
					isAllDay = false
				)
			),
			selectedDay = LocalDate.now(),
			onDismissRequest = {}
		)
	}
}