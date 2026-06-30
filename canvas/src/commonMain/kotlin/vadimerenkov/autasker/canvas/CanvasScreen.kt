package vadimerenkov.autasker.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.core.presentation.components.TaskItem
import vadimerenkov.autasker.core.presentation.main.MainAction
import java.util.Random

@Composable
fun CanvasScreen(
	onTaskClick: (Long) -> Unit,
	viewModel: CanvasViewModel = koinViewModel()
) {
	CanvasRoot(
		state = viewModel.state,
		onAction = { action ->
			when (action) {
				is MainAction.OnTaskClick -> onTaskClick(action.id)
				else -> Unit
			}
			viewModel.onAction(action)
		}
	)
}

@Composable
private fun CanvasRoot(
	state: CanvasState,
	onAction: (MainAction) -> Unit
) {
	var offset by remember { mutableStateOf(IntOffset.Zero) }
	MindMap(
		mapOffset = offset,
		random = Random(123),
		onDrag = {
			offset += it
		},
		modifier = Modifier
			.background(MaterialTheme.colorScheme.background)
			.clip(RectangleShape)
	) {
		state.tasks.forEach { task ->
			TaskItem(
				task = task,
				canOpenSubtasks = false,
				onAction = onAction,
				modifier = Modifier
					.widthIn(max = 300.dp)
			)
		}
	}
}