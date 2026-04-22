package vadimerenkov.autasker.core.presentation.extensions

import androidx.compose.runtime.Composable
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.critical
import autasker.core.presentation.generated.resources.high
import autasker.core.presentation.generated.resources.normal
import autasker.core.presentation.generated.resources.very_high
import org.jetbrains.compose.resources.stringResource

@Composable
fun Int.toImportance(): String {
	return when (this) {
		1 -> stringResource(Res.string.high)
		2 -> stringResource(Res.string.very_high)
		3 -> stringResource(Res.string.critical)
		else -> stringResource(Res.string.normal)
	}
}