package vadimerenkov.autasker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun DataChip(
	text: String,
	modifier: Modifier = Modifier,
	onCrossClick: () -> Unit = {},
	color: Color = rememberRandomColor(),
	showCross: Boolean = true,
	leadingIcon: @Composable () -> Unit = {},
	onClick: () -> Unit = {},
	clickable: Boolean = true,
	buttonDescription: String? = null,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
			.clip(RoundedCornerShape(8.dp))
			.background(color.copy(alpha = 0.2f))
			.clickable(enabled = clickable) {
				onClick()
			}
			.padding(4.dp)
	) {
		leadingIcon()
		Text(
			text = text,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier
				.padding(horizontal = 4.dp)
		)
		if (showCross) {
			IconButton(
				onClick = onCrossClick,
				modifier = Modifier
					.size(32.dp)
			) {
				Icon(
					imageVector = Icons.Default.Close,
					contentDescription = buttonDescription,
					tint = Color.Black.copy(alpha = 0.3f)
				)
			}
		}
	}
}

fun Random.nextColor(): Color {
	return Color(nextInt(256), nextInt(256), nextInt(256))
}

@Composable
fun rememberRandomColor(): Color {
	return remember { Random.nextColor() }
}