package vadimerenkov.autasker.common.settings.enums

import autasker.common.generated.resources.Res
import autasker.common.generated.resources.dark_theme
import autasker.common.generated.resources.device_theme
import autasker.common.generated.resources.light_theme
import org.jetbrains.compose.resources.StringResource

enum class Theme(
	val stringRes: StringResource
) {
	LIGHT(Res.string.light_theme),
	DARK(Res.string.dark_theme),
	DEVICE(Res.string.device_theme)
}