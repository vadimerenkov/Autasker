package vadimerenkov.autasker.presentation.new_day

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.what_tasks_today
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinMultiplatformApplication
import org.koin.dsl.koinConfiguration
import vadimerenkov.autasker.di.appModule
import vadimerenkov.autasker.di.platformModule
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.presentation.theme.AutaskerTheme
import java.time.ZonedDateTime

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

@Composable
@Preview
private fun YesterdayPreview() {
	AutaskerTheme {
		KoinMultiplatformApplication( config = koinConfiguration { modules(appModule, platformModule)}) {
			TodayTab(
				onClick = {},
				tasks = listOf(
					Task(
						id = 0,
						categoryId = 1,
						index = 0,
						title = "Here is my task"
					),
					Task(
						id = 1,

						categoryId = 1,
						index = 1,
						title = "And here is another",
						description = "With the description even"
					),
					Task(
						id = 2,

						categoryId = 1,
						index = 2,
						title = "Beware of your friend, Palpatine",
						isCompleted = true
					),
					Task(
						id = 3,

						categoryId = 1,
						index = 4,
						title = "...and your pal, Friendpatine",
						isCompleted = false
					),
					Task(
						id = 4,

						categoryId = 1,
						index = 3,
						dueDate = ZonedDateTime.now(),
						title = "Forrest Day - Hyperactive"
					),
				)
			)
		}
	}
}