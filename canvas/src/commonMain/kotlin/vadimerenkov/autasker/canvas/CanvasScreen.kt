package vadimerenkov.autasker.canvas

import androidx.compose.foundation.background
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
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.core.presentation.components.TaskItem
import java.util.Random

@Composable
fun CanvasScreen(
	viewModel: CanvasViewModel = koinViewModel()
) {
	CanvasRoot(
		state = viewModel.state
	)
}

@Composable
private fun CanvasRoot(
	state: CanvasState
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
				onAction = {}
			)
		}
	}
}