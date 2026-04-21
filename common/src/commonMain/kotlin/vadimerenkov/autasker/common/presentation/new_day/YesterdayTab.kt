package vadimerenkov.autasker.common.presentation.new_day

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.completed
import autasker.common.generated.resources.day_is_over
import autasker.common.generated.resources.set_for_today
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.Time
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
							.fillMaxWidth()
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
							text = stringResource(Res.string.completed)
						)
					}
					val isToday = task.dueDate?.toLocalDate() == Time.today()
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
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
							text = stringResource(Res.string.set_for_today)
						)
					}
					HorizontalDivider()
				}
			}
		}
	}
}