package vadimerenkov.autasker.settings.enums

import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.dark_theme
import autasker.composeapp.generated.resources.device_theme
import autasker.composeapp.generated.resources.light_theme
import org.jetbrains.compose.resources.StringResource

enum class Theme(
	val stringRes: StringResource
) {
	LIGHT(Res.string.light_theme),
	DARK(Res.string.dark_theme),
	DEVICE(Res.string.device_theme)
}