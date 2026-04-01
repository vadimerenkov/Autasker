package vadimerenkov.autasker.settings.enums

import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.ddmmyy
import autasker.composeapp.generated.resources.mmddyy
import autasker.composeapp.generated.resources.yymmdd
import org.jetbrains.compose.resources.StringResource

enum class DateFormat(
	val stringRes: StringResource

) {
	YYYYMMDD(Res.string.yymmdd),
	DDMMYYYY(Res.string.ddmmyy),
	MMDDYYYY(Res.string.mmddyy)
}