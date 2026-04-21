package vadimerenkov.autasker.common.settings.enums

import autasker.common.generated.resources.Res
import autasker.common.generated.resources.english
import autasker.common.generated.resources.russian
import org.jetbrains.compose.resources.StringResource

enum class Language(
	val code: String,
	val stringRes: StringResource
) {
	ENGLISH("en-US", Res.string.english),
	RUSSIAN("ru-RU", Res.string.russian)
}