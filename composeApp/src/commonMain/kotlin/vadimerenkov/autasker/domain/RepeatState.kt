package vadimerenkov.autasker.domain

import androidx.compose.runtime.Composable
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.mode_completion
import autasker.composeapp.generated.resources.mode_completion_desc
import autasker.composeapp.generated.resources.mode_exact
import autasker.composeapp.generated.resources.mode_exact_desc
import org.jetbrains.compose.resources.StringResource
import java.time.DayOfWeek

data class RepeatState(
	val isRepeating: Boolean = false,
	val mode: RepeatMode = RepeatMode.ON_EXACT,
	val period: Period = Period.DAY,
	val times: Long = 1L,
	val weekDays: List<DayOfWeek> = emptyList(),
)

enum class RepeatMode(
	val title: StringResource,
	val description: StringResource
) {
	ON_COMPLETION(
		title = Res.string.mode_completion,
		description = Res.string.mode_completion_desc
	),
	ON_EXACT(
		title = Res.string.mode_exact,
		description = Res.string.mode_exact_desc
	),
	/*
	FORGIVING(
		title = Res.string.mode_forgiving,
		description = Res.string.mode_forgiving_desc
	),
	ALWAYS(
		title = Res.string.mode_always,
		description = Res.string.mode_always_desc
	)

	 */
}

@Composable
fun RepeatState.formatted(): String {
	val every = period.getWordEvery(times.toInt())
	val period = period.toLocalizedString(times.toInt())
	return if (times == 1L) "$every $period" else "$every $times $period"
}