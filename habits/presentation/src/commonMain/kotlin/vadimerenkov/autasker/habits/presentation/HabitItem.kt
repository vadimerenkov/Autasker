package vadimerenkov.autasker.habits.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.delete_habit
import autasker.core.presentation.generated.resources.edit_habit
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme

@Composable
fun HabitItem(
	habit: Habit,
	isSelected: Boolean = false,
	onAction: (HabitsAction) -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				onAction(HabitsAction.OnHabitClick(habit.id))
			}
			.background(
				color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent
			)
	) {
		Text(
			text = habit.title,
			modifier = Modifier
				.padding(16.dp)
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
		IconButton(
			onClick = {
				onAction(HabitsAction.DeleteHabitClick(habit.id))
			}
		) {
			Icon(
				imageVector = Icons.Default.Delete,
				contentDescription = stringResource(Res.string.delete_habit)
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