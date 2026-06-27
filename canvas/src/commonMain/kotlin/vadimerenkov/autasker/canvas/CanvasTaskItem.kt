package vadimerenkov.autasker.canvas

import androidx.compose.ui.geometry.Offset
import vadimerenkov.autasker.core.domain.Task

data class CanvasTaskItem(
	val task: Task,
	val percentageOffset: Offset = Offset(0f, 0f)
)
