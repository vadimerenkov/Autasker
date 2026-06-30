package vadimerenkov.autasker.canvas

import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.round
import java.util.Random
import kotlin.math.roundToInt

@Composable
fun MindMap(
	mapOffset: IntOffset,
	random: Random,
	onDrag: (delta: IntOffset) -> Unit,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Layout(
		content = content,
		modifier = modifier
			.draggable2D(
				state = rememberDraggable2DState { delta ->
					onDrag(delta.round())
				}
			)
	) { measurables, constraints ->
		val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }
		val centerX = constraints.maxWidth / 2
		val centerY = constraints.maxHeight / 2

		val alreadyPlaced = mutableListOf<IntRect>()

		layout(constraints.maxWidth, constraints.maxHeight) {
			placeables.forEach { placeable ->
				var isPlaced = false
				var attempt = 10

				while (!isPlaced) {
					val offsetX = (random.nextGaussian() * attempt).roundToInt()
					val offsetY = (random.nextGaussian() * attempt).roundToInt()

					val finalX = centerX - placeable.width / 2 + offsetX
					val finalY = centerY - placeable.height / 2 + offsetY

					val rect = IntRect(finalX, finalY, finalX + placeable.width, finalY + placeable.height)

					if (alreadyPlaced.any { it.overlaps(rect) }) {
						attempt += 10
					} else {
						alreadyPlaced.add(rect)
						placeable.place(
							x = finalX + mapOffset.x,
							y = finalY + mapOffset.y
						)
						isPlaced = true
					}

				}
			}
		}
	}
}