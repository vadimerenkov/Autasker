package vadimerenkov.autasker.settings.enums

import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.english
import autasker.composeapp.generated.resources.russian
import org.jetbrains.compose.resources.StringResource

enum class Language(
	val code: String,
	val stringRes: StringResource
) {
	ENGLISH("en-US", Res.string.english),
	RUSSIAN("ru-RU", Res.string.russian)
}