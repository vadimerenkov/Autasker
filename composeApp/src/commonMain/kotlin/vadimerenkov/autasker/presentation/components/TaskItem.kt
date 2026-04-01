package vadimerenkov.autasker.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.presentation.main.MainAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
expect fun TaskItem(
	task: Task,
	onAction: (MainAction) -> Unit,
	modifier: Modifier = Modifier
)