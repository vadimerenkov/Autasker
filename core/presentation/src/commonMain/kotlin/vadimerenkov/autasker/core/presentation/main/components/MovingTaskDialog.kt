package vadimerenkov.autasker.core.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import vadimerenkov.autasker.core.presentation.main.MainAction
import vadimerenkov.autasker.core.presentation.main.MainState

@Composable
fun MovingTaskDialog(
	movingTaskId: Long,
	state: MainState,
	onAction: (MainAction) -> Unit,
	onDismissRequest: () -> Unit
) {
	Dialog(
		onDismissRequest = onDismissRequest
	) {
		Column(
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.background(MaterialTheme.colorScheme.background)
				.padding(16.dp)
		) {
			state.categories.forEach { category ->
				Text(
					text = category.title ?: "<Unnamed column>",
					modifier = Modifier
						.clip(RoundedCornerShape(8.dp))
						.clickable {
							onAction(
								MainAction.MoveTaskCategoryChosen(
									movingTaskId,
									category.id
								)
							)
							onDismissRequest()
						}
						.padding(12.dp)
				)
			}
		}
	}
}