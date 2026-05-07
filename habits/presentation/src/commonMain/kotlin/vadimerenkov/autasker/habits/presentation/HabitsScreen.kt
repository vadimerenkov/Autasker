package vadimerenkov.autasker.habits.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.habits.presentation.navigation.HabitDetailRoute
import vadimerenkov.autasker.habits.presentation.navigation.HabitListRoute
import vadimerenkov.autasker.habits.presentation.navigation.ListDetailScene
import vadimerenkov.autasker.habits.presentation.navigation.rememberListDetailSceneStrategy

@Composable
fun HabitsScreen(
	modifier: Modifier = Modifier,
	viewModel: HabitsViewModel = koinViewModel()
) {
	HabitsScreenRoot(
		state = viewModel.state,
		onAction = viewModel::onAction,
		modifier = modifier
	)
}

@Composable
private fun HabitsScreenRoot(
	state: HabitsState,
	onAction: (HabitsAction) -> Unit,
	modifier: Modifier = Modifier
) {
	val backstack = remember { mutableStateListOf<NavKey>(HabitListRoute) }
	NavDisplay(
		backStack = backstack,
		sceneStrategies = listOf(rememberListDetailSceneStrategy()),
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator()
		),
		entryProvider = entryProvider {
			entry<HabitListRoute>(
				metadata = ListDetailScene.list()
			) {
				HabitListScreen(
					state = state,
					onAction = { action ->
						onAction(action)
						when (action) {
							is HabitsAction.OnHabitClick -> {
								backstack.add(HabitDetailRoute)
							}
							else -> Unit
						}
					}
				)
			}
			entry<HabitDetailRoute>(
				metadata = ListDetailScene.detail()
			) {
				HabitDetailsScreen(
					habit = state.selectedHabit!!,
					completions = state.completions.filter { it.habitId == state.selectedHabit.id },
					onAction = onAction
				)
			}
		}
	)
}