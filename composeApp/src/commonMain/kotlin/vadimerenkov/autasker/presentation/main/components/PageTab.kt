package vadimerenkov.autasker.presentation.main.components

import androidx.compose.runtime.Composable
import vadimerenkov.autasker.domain.Page
import vadimerenkov.autasker.presentation.main.MainAction
import vadimerenkov.autasker.presentation.main.MainState

@Composable
expect fun PageTab(
	page: Page,
	state: MainState,
	onTaskAction: (MainAction) -> Unit
)