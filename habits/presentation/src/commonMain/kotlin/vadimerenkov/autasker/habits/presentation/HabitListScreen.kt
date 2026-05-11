package vadimerenkov.autasker.habits.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HabitListScreen(
	state: HabitsState,
	onAction: (HabitsAction) -> Unit,
	modifier: Modifier = Modifier
) {
	LazyColumn(
		modifier = modifier
	) {
		items(
			items = state.habits,
			key = { it.id }
		) { habit ->
			HabitItem(
				habit = habit,
				isSelected = habit.id == state.selectedHabit?.id,
				onAction = onAction
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
}