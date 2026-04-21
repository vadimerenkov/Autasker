package vadimerenkov.autasker.common.presentation.new_day

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.what_tasks_today
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.common.domain.Task

@Composable
internal fun TodayTab(
	tasks: List<Task>,
	onClick: (id: Long) -> Unit,
	modifier: Modifier = Modifier
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		Text(
			text = stringResource(Res.string.what_tasks_today)
		)
		LazyColumn(
			contentPadding = PaddingValues(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			items(
				items = tasks,
				key = { it.id }
			) { task ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.clickable {
							onClick(task.id)
						}
				) {
					RadioButton(
						selected = task.dueDate != null,
						onClick = {
							onClick(task.id)
						},
						modifier = Modifier
							.padding(start = 16.dp)
					)
					Text(
						text = task.title
					)

				}
			}
		}
	}
}