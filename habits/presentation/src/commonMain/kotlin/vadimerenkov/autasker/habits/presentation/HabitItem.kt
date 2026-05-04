package vadimerenkov.autasker.habits.presentation

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme

@Composable
fun HabitItem(
	habit: Habit,
	onClick: (Long) -> Unit
) {
	Text(
		text = habit.title,
		modifier = Modifier
			.clickable {
				onClick(habit.id)
			}
	)
}

@Composable
@Preview
private fun HabitItemPreview() {
	AutaskerTheme {
		HabitItem(
			habit = Habit(),
			onClick = {}
		)
	}
}