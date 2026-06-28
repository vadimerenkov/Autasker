package vadimerenkov.autasker.habits.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.new_habit
import org.jetbrains.compose.resources.stringResource

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
				},
				modifier = Modifier
					.padding(16.dp)
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = stringResource(Res.string.new_habit)
				)
			}
		}
	}
}