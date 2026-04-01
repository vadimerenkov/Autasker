package vadimerenkov.autasker.presentation.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState

@Composable
fun CommonWindow(
	title: String,
	onCloseRequest: () -> Unit,
	content: @Composable () -> Unit
) {
	Window(
		onCloseRequest = onCloseRequest,
		title = title,
		state = rememberWindowState(
			position = WindowPosition.Aligned(Alignment.Center)
		)
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.padding(16.dp)
		) {
			content()
		}
	}
}