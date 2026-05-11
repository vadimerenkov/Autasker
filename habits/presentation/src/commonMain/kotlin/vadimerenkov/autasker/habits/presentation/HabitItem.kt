package vadimerenkov.autasker.habits.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.edit_habit
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme

@Composable
fun HabitItem(
	habit: Habit,
	onAction: (HabitsAction) -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				onAction(HabitsAction.OnHabitClick(habit.id))
			}
	) {
		Text(
			text = habit.title
		)
		Spacer(modifier = Modifier.weight(1f))
		IconButton(
			onClick = {
				onAction(HabitsAction.EditHabitClick(habit.id))
			}
		) {
			Icon(
				imageVector = Icons.Default.Edit,
				contentDescription = stringResource(Res.string.edit_habit)
			)
		}
	}
}

@Composable
@Preview
private fun HabitItemPreview() {
	AutaskerTheme {
		HabitItem(
			habit = Habit(),
			onAction = {}
		)
	}
}