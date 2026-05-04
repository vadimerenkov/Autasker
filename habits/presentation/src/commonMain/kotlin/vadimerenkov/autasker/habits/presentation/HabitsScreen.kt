package vadimerenkov.autasker.habits.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

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
	Row(
		modifier = Modifier
			.fillMaxSize()
	) {
		LazyColumn(
			modifier = Modifier
				.weight(1f)
		) {
			items(
				items = state.habits,
				key = { it.id }
			) { habit ->
				HabitItem(
					habit = habit,
					onClick = {
						onAction(HabitsAction.OnHabitClick(it))
					}
				)
			}
			item {
				Button(
					onClick = {
						onAction(HabitsAction.NewHabitClick)
					}
				) {
					Text(
						text = "+"
					)
				}
			}
		}
		state.selectedHabit?.let { habit ->
			HabitDetailsScreen(
				habit = habit,
				completions = state.completions.filter { it.habitId == habit.id },
				onAction = onAction,
				modifier = Modifier
					.weight(1f)
			)
		}
	}
}