package vadimerenkov.autasker.core.presentation.extensions

import androidx.compose.runtime.Composable
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.mode_arbitrary
import autasker.core.presentation.generated.resources.mode_arbitrary_desc
import autasker.core.presentation.generated.resources.mode_completion
import autasker.core.presentation.generated.resources.mode_completion_desc
import autasker.core.presentation.generated.resources.mode_exact
import autasker.core.presentation.generated.resources.mode_exact_desc
import org.jetbrains.compose.resources.StringResource
import vadimerenkov.autasker.core.domain.RepeatMode
import vadimerenkov.autasker.core.domain.RepeatState

val RepeatMode.title: StringResource
	get() {
		return when (this) {
			RepeatMode.ON_COMPLETION -> Res.string.mode_completion
			RepeatMode.ON_EXACT -> Res.string.mode_exact
			RepeatMode.ARBITRARY -> Res.string.mode_arbitrary
		}
	}

val RepeatMode.description: StringResource
	get() = when (this) {
		RepeatMode.ON_COMPLETION -> Res.string.mode_completion_desc
		RepeatMode.ON_EXACT -> Res.string.mode_exact_desc
		RepeatMode.ARBITRARY -> Res.string.mode_arbitrary_desc
	}

@Composable
fun RepeatState.formatted(): String {
	val every = period.getWordEvery(times.toInt())
	val period = period.toLocalizedString(times.toInt())
	return if (times == 1L) "$every $period" else "$every $times $period"
}