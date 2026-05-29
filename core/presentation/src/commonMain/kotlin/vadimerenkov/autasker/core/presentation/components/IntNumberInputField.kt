package vadimerenkov.autasker.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IntNumberInputField(
	value: Int?,
	onValueChange: (Int?) -> Unit,
	modifier: Modifier = Modifier,
	isError: Boolean = false,
	enabled: Boolean = true,
	minNumber: Int = Int.MIN_VALUE,
	maxNumber: Int = Int.MAX_VALUE,
	showButtons: Boolean = true
) {
	val state = rememberTextFieldState(value?.toString() ?: "")

	LaunchedEffect(value) {
		state.edit {
			replace(0, length, value?.toString() ?: "")
		}
	}

	Box(
		modifier = modifier
	) {
		OutlinedTextField(
			state = state,
			inputTransformation = InputTransformation.then {
				println(this.toString())
				try {
					if (asCharSequence().isEmpty()) {
						println("Empty, return null")
						onValueChange(null)
					} else {
						onValueChange(toString().toInt())
					}
				}
				catch (e: NumberFormatException) {
					revertAllChanges()
				}
			},
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Number
			),
			placeholder = {
				Text(
					text = "0",
					color = Color.Black.copy(alpha = 0.3f)
				)
			},
			isError = isError,
			enabled = enabled,
			modifier = Modifier
				.onFocusChanged {
					if (!it.hasFocus && value != null) {
						onValueChange(value.coerceIn(minNumber, maxNumber))
					}
				}
		)
		if (showButtons) {
			Column(
				modifier = Modifier
					.align(Alignment.CenterEnd)
					.padding(end = 8.dp)
			) {
				val plusEnabled = if (value == null) true else value < maxNumber
				val minusEnabled = value != null && value > minNumber
				CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
					Icon(
						imageVector = Icons.Default.ArrowDropUp,
						contentDescription = null,
						tint = if (plusEnabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
						modifier = Modifier
							.clickable(
								enabled = enabled && plusEnabled
							) {
								onValueChange(
									value?.plus(1)?.coerceIn(minNumber, maxNumber) ?: minNumber
								)
							}
					)

					Icon(
						imageVector = Icons.Default.ArrowDropDown,
						contentDescription = null,
						tint = if (minusEnabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
						modifier = Modifier
							.clickable(
								enabled = enabled && minusEnabled
							) {
								onValueChange(value?.minus(1)?.coerceIn(minNumber, maxNumber))
							}
					)
				}
			}
		}
	}
}