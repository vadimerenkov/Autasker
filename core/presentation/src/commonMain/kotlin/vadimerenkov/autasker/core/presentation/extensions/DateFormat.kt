package vadimerenkov.autasker.core.presentation.extensions

import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.ddmmyy
import autasker.core.presentation.generated.resources.mmddyy
import autasker.core.presentation.generated.resources.yymmdd
import org.jetbrains.compose.resources.StringResource
import vadimerenkov.autasker.core.domain.settings.enums.DateFormat

val DateFormat.stringRes: StringResource
	get() = when (this) {
		DateFormat.YYYYMMDD -> Res.string.yymmdd
		DateFormat.DDMMYYYY -> Res.string.ddmmyy
		DateFormat.MMDDYYYY -> Res.string.mmddyy
	}