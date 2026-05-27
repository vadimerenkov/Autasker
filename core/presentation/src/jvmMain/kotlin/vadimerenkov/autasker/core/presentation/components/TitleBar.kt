package vadimerenkov.autasker.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class MenuAction(
	val text: String,
	val showIndicator: Boolean = false,
	val onClick: () -> Unit
)

@Composable
fun TitleBar(
	actions: List<MenuAction>,
	modifier: Modifier = Modifier,
) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = modifier
				.fillMaxWidth()
		) {
			actions.forEach { action ->
				Box(
					modifier = Modifier
						.clickable {
							action.onClick()
						}
				) {
					Text(
						text = action.text,
						color = MaterialTheme.colorScheme.onBackground,
						modifier = Modifier
							.padding(horizontal = 16.dp)
							.padding(vertical = 8.dp)
					)
					if (action.showIndicator) {
						Box(
							modifier = Modifier
								.padding(4.dp)
								.size(12.dp)
								.clip(CircleShape)
								.background(MaterialTheme.colorScheme.primary)
								.align(Alignment.TopEnd)
						)

					}
				}
			}
		}
	}

@Preview
@Composable
private fun TitleBarPreview() {
	TitleBar(
		actions = listOf(
			MenuAction(
				text = "Settings",
				onClick = {}
			),
			MenuAction(
				text = "About",
				onClick = {}
			)
		)
	)
}