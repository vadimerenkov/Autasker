package vadimerenkov.autasker.core.presentation.main.components

import androidx.compose.runtime.Composable
import vadimerenkov.autasker.core.domain.Page
import vadimerenkov.autasker.core.presentation.main.MainAction
import vadimerenkov.autasker.core.presentation.main.MainState

@Composable
expect fun PageTab(
	page: Page,
	state: MainState,
	onTaskAction: (MainAction) -> Unit
)