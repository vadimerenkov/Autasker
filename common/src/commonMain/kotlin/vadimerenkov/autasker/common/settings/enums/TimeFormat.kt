package vadimerenkov.autasker.common.settings.enums

import autasker.common.generated.resources.Res
import autasker.common.generated.resources._12_hour
import autasker.common.generated.resources._24_hour
import org.jetbrains.compose.resources.StringResource

enum class TimeFormat(
	val stringRes: StringResource
) {
	CLOCK_12(Res.string._12_hour),
	CLOCK_24(Res.string._24_hour)
}