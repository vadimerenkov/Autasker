package vadimerenkov.autasker.core.presentation.main.components

import androidx.compose.runtime.Composable
import vadimerenkov.autasker.common.domain.Page
import vadimerenkov.autasker.common.presentation.main.MainAction
import vadimerenkov.autasker.common.presentation.main.MainState

@Composable
expect fun PageTab(
	page: Page,
	state: MainState,
	onTaskAction: (MainAction) -> Unit
)