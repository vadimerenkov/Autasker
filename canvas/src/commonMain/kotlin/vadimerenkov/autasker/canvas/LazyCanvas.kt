package vadimerenkov.autasker.canvas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import vadimerenkov.autasker.core.presentation.components.TaskItem
import kotlin.math.roundToInt

private data class CanvasItem(
	val placeable: Placeable,
	val finalX: Int,
	val finalY: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyCanvas(
	items: List<CanvasTaskItem>,
	onDrag: (delta: IntOffset) -> Unit,
	modifier: Modifier = Modifier,
	maxItemWidth: Dp = 300.dp,
	maxItemHeight: Dp = 100.dp,
	offset: IntOffset = IntOffset.Zero
) {
	LazyLayout(
		modifier = modifier
			.draggable2D(
				state = rememberDraggable2DState { delta ->
					onDrag(delta.round())
				}
			),
		itemProvider = {
			object : LazyLayoutItemProvider {

				override val itemCount: Int
					get() = items.size

				@Composable
				override fun Item(index: Int, key: Any) {
					val task = items[index].task
					TaskItem(
						task = task,
						onAction = {}
					)
				}
			}
		}
	) { constraints ->
		val layoutWidth = constraints.maxWidth
		val layoutHeight = constraints.maxHeight

		val visibleArea = IntRect(
			left = 0,
			top = 0,
			right = layoutWidth,
			bottom = layoutHeight
		)

		val visibleItems = items.mapIndexedNotNull { index, item ->
			val finalX = (item.percentageOffset.x * layoutWidth + layoutWidth / 2 + offset.x).roundToInt()
			val finalY = (item.percentageOffset.y * layoutHeight + layoutHeight / 2 + offset.y).roundToInt()

			val maxWidth = maxItemWidth.roundToPx()
			val maxHeight = maxItemHeight.roundToPx()
			val extendedItemBounds = IntRect(
				left = finalX - maxWidth / 2,
				top = finalY - maxHeight / 2,
				right = finalX + 3 * (maxWidth / 2),
				bottom = finalY + 3 * (maxHeight / 2)
			)

			if (visibleArea.overlaps(extendedItemBounds)) {
				val placeable = measure(
					index = index,
					constraints = Constraints()
				)
				CanvasItem(
					placeable = placeable.first(),
					finalX = finalX,
					finalY = finalY
				)
			} else null
		}

		layout(constraints.maxWidth, constraints.maxHeight) {
			visibleItems.forEach { item ->
				item.placeable.place(
					x = item.finalX - item.placeable.width / 2,
					y = item.finalY - item.placeable.height / 2
				)
			}
		}
	}
}