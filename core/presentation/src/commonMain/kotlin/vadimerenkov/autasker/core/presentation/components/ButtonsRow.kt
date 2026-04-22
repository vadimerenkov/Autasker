package vadimerenkov.autasker.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.cancel
import autasker.core.presentation.generated.resources.save
import org.jetbrains.compose.resources.stringResource

@Composable
fun ButtonsRow(
	modifier: Modifier = Modifier,
	primaryText: String = stringResource(Res.string.save),
	onPrimaryClick: () -> Unit,
	isPrimaryButtonEnabled: Boolean = true,
	secondaryText: String = stringResource(Res.string.cancel),
	onSecondaryClick: () -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = modifier
//			.widthIn(max = 700.dp)
			.fillMaxWidth()
	) {
		PrimaryButton(
			onClick = onPrimaryClick,
			text = primaryText,
			isEnabled = isPrimaryButtonEnabled,
			modifier = Modifier
				.weight(1f)
		)
		Spacer(modifier = Modifier.width(8.dp))
		SecondaryButton(
			onClick = onSecondaryClick,
			text = secondaryText,
			modifier = Modifier
				.weight(1f)
		)
	}
}

@Composable
@Preview
private fun ButtonsRowPreview() {
	ButtonsRow(
		onPrimaryClick = {},
		onSecondaryClick = {}
	)
}