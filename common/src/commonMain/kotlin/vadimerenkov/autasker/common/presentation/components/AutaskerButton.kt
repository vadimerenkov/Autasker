package vadimerenkov.autasker.common.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
	onClick: () -> Unit,
	text: String,
	modifier: Modifier = Modifier,
	isEnabled: Boolean = true
) {
	Button(
		onClick = onClick,
		shape = RoundedCornerShape(12.dp),
		enabled = isEnabled,
		modifier = modifier
	) {
		Text(
			text = text
		)
	}
}

@Composable
fun SecondaryButton(
	onClick: () -> Unit,
	text: String,
	modifier: Modifier = Modifier,
	isEnabled: Boolean = true
) {
	OutlinedButton(
		enabled = isEnabled,
		onClick = onClick,
		shape = RoundedCornerShape(12.dp),
		modifier = modifier
	) {
		Text(
			text = text
		)
	}
}

@Composable
@Preview
private fun ButtonPreview() {
	Column {
		PrimaryButton(
			onClick = {},
			text = "Save",
			modifier = Modifier
				.padding(16.dp)
		)
		SecondaryButton(
			onClick = {},
			text = "Cancel",
			modifier = Modifier
				.padding(16.dp)
		)
	}
}