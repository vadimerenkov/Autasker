package vadimerenkov.autasker.presentation.task_edit.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Deprecated("")
@Composable
fun CalendarRow(
	leftColumn: @Composable () -> Unit,
	rightColumn: @Composable () -> Unit,
	modifier: Modifier = Modifier
) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
//		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier
				.weight(1f)
		) {
			leftColumn()
		}
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.weight(3f)
		) {
			rightColumn()
		}
	}
}