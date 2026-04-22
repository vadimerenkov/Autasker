package vadimerenkov.autasker.core.presentation.extensions

import androidx.compose.ui.graphics.Color
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.lavender
import autasker.core.presentation.generated.resources.sea
import org.jetbrains.compose.resources.StringResource
import vadimerenkov.autasker.core.domain.settings.enums.ThemeColor
import vadimerenkov.autasker.core.presentation.theme.blue_primaryDark
import vadimerenkov.autasker.core.presentation.theme.blue_primaryLight
import vadimerenkov.autasker.core.presentation.theme.violet_primaryDark
import vadimerenkov.autasker.core.presentation.theme.violet_primaryLight

val ThemeColor.nameRes: StringResource
	get() = when (this) {
		ThemeColor.VIOLET -> Res.string.lavender
		ThemeColor.BLUE -> Res.string.sea
	}

val ThemeColor.lightColor: Color
	get() = when (this) {
		ThemeColor.VIOLET -> violet_primaryLight
		ThemeColor.BLUE -> blue_primaryLight
	}

val ThemeColor.darkColor: Color
	get() = when (this) {
		ThemeColor.VIOLET -> violet_primaryDark
		ThemeColor.BLUE -> blue_primaryDark
	}