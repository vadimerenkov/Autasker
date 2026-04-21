package vadimerenkov.autasker.common.settings.enums

import autasker.common.generated.resources.Res
import autasker.common.generated.resources.ddmmyy
import autasker.common.generated.resources.mmddyy
import autasker.common.generated.resources.yymmdd
import org.jetbrains.compose.resources.StringResource

enum class DateFormat(
	val stringRes: StringResource

) {
	YYYYMMDD(Res.string.yymmdd),
	DDMMYYYY(Res.string.ddmmyy),
	MMDDYYYY(Res.string.mmddyy)
}