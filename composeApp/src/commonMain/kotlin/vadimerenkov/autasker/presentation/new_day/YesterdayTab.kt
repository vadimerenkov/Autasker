package vadimerenkov.autasker.presentation.new_day

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.day_is_over
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.domain.Time
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun YesterdayTab(
	tasks: List<Task>,
	onCompletedClick: (id: Long) -> Unit,
	onSetTodayClick: (id: Long, isSet: Boolean) -> Unit,
	modifier: Modifier = Modifier
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		val weekday = Time
			.yesterday()
			.dayOfWeek
			.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
		Text(
			text = stringResource(Res.string.day_is_over, weekday)
		)
		LazyColumn(
			contentPadding = PaddingValues(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			items(
				items = tasks,
				key = { it.id }
			) { task ->
				Column {
					Text(
						text = task.title
					)
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.clickable {
								onCompletedClick(task.id)
							}
					) {
						RadioButton(
							selected = task.isCompleted,
							onClick = {
								onCompletedClick(task.id)
							}
						)
						Text(
							text = "Completed"
						)
					}
					val isToday = task.dueDate?.toLocalDate() == Time.today()
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.clickable {
								onSetTodayClick(task.id, !isToday)
							}
					) {

						RadioButton(
							selected = isToday,
							onClick = {
								onSetTodayClick(task.id, !isToday)
							}
						)
						Text(
							text = "Set for today"
						)
					}
					HorizontalDivider()
				}
			}
		}
	}
}