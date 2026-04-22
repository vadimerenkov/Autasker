package vadimerenkov.autasker.core.presentation.extensions

import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources._12_hour
import autasker.core.presentation.generated.resources._24_hour
import org.jetbrains.compose.resources.StringResource
import vadimerenkov.autasker.core.domain.settings.enums.TimeFormat

val TimeFormat.stringRes: StringResource
	get() = when (this) {
		TimeFormat.CLOCK_12 -> Res.string._12_hour
		TimeFormat.CLOCK_24 -> Res.string._24_hour
	}