package vadimerenkov.autasker.core.presentation.extensions

import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.dark_theme
import autasker.core.presentation.generated.resources.device_theme
import autasker.core.presentation.generated.resources.light_theme
import org.jetbrains.compose.resources.StringResource
import vadimerenkov.autasker.core.domain.settings.enums.Theme

val Theme.stringRes: StringResource
	get() = when (this) {
		Theme.LIGHT -> Res.string.light_theme
		Theme.DARK -> Res.string.dark_theme
		Theme.DEVICE -> Res.string.device_theme
	}