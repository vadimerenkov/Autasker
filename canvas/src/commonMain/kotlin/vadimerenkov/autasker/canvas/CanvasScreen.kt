package vadimerenkov.autasker.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import org.koin.compose.viewmodel.koinViewModel

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
	LazyCanvas(
		items = state.tasks,
		offset = offset,
		onDrag = { delta ->
			offset += delta
		},
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	)
}