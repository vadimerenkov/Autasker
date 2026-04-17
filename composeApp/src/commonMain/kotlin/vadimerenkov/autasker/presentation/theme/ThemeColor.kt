package vadimerenkov.autasker.presentation.theme

import androidx.compose.ui.graphics.Color
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.lavender
import autasker.composeapp.generated.resources.sea
import org.jetbrains.compose.resources.StringResource

enum class ThemeColor(
	val nameRes: StringResource,
	val lightColor: Color,
	val darkColor: Color
) {
	VIOLET(
		nameRes = Res.string.lavender,
		lightColor = violet_primaryLight,
		darkColor = violet_primaryDark
	),
	BLUE(
		nameRes = Res.string.sea,
		lightColor = blue_primaryLight,
		darkColor = blue_primaryDark
	)
}