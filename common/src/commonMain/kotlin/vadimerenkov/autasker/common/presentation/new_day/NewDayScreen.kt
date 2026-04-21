package vadimerenkov.autasker.common.presentation.new_day

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.finish
import autasker.common.generated.resources.next
import autasker.common.generated.resources.previous
import autasker.common.generated.resources.start_new_day
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.presentation.components.PrimaryButton
import vadimerenkov.autasker.common.presentation.components.SecondaryButton
import vadimerenkov.autasker.common.presentation.theme.AutaskerTheme

@Composable
fun NewDayScreen(
	modifier: Modifier = Modifier,
	viewModel: NewDayViewModel = koinViewModel()
) {
	NewDayScreenRoot(
		state = viewModel.state,
		modifier = modifier,
		onAction = viewModel::onAction
	)
}

@Composable
private fun NewDayScreenRoot(
	onAction: (NewDayAction) -> Unit,
	modifier: Modifier = Modifier,
	state: NewDayState = NewDayState()
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
			.fillMaxSize()
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.heightIn(max = 500.dp)
		) {

			Text(
				text = stringResource(Res.string.start_new_day)
			)
			AnimatedContent(targetState = state.currentTab) {
				when (it) {
					Tab.YESTERDAY_TASKS -> {
						YesterdayTab(
							tasks = state.yesterdayTasks,
							onCompletedClick = { id ->
								onAction(NewDayAction.TaskClick(id))
							},
							onSetTodayClick = { id, isSet ->
								onAction(NewDayAction.SetTodayClick(id, isSet))
							}
						)
					}

					Tab.TODAY_TASKS -> {
						TodayTab(
							tasks = state.todayTasks,
							onClick = { id ->
								onAction(NewDayAction.TaskClick(id))
							}
						)
					}

					null -> {
						CircularProgressIndicator()
					}
				}
			}
		}
		Spacer(modifier = Modifier.weight(1f))
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier
				.fillMaxWidth()
		) {
			val progress by animateFloatAsState(((state.currentTabIndex + 1) / state.listOfTabs.size.toFloat()))
			SecondaryButton(
				isEnabled = state.listOfTabs.firstOrNull() != state.currentTab,
				onClick = {
					onAction(NewDayAction.PreviousButtonClick)
				},
				text = stringResource(Res.string.previous)
			)
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "${state.currentTabIndex + 1} / ${state.listOfTabs.size}"
				)
				LinearProgressIndicator(
					drawStopIndicator = {},
					progress = {
						progress
					}
				)
			}
			PrimaryButton(
				onClick = {
					onAction(NewDayAction.NextButtonClick)
				},
				text = if (state.currentTabIndex + 1 == state.listOfTabs.size) {
					stringResource(Res.string.finish)
				} else {
					stringResource(Res.string.next)
				}
			)
		}
	}
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun NewDayScreenPreview() {
//	KoinApplicationPreview(application = { modules(appModule, platformModule)}) {
		AutaskerTheme {
			NewDayScreenRoot(
				onAction = {},
				state = NewDayState(
					todayTasks = listOf(Task(id = 0), Task(id = 1)),
					currentTab = Tab.TODAY_TASKS,
					listOfTabs = listOf(Tab.YESTERDAY_TASKS, Tab.TODAY_TASKS)
				)
			)
		}
	}
//}