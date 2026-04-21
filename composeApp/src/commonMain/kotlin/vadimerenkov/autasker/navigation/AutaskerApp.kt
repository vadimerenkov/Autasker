package vadimerenkov.autasker.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import vadimerenkov.autasker.common.presentation.theme.AutaskerTheme
import vadimerenkov.autasker.common.settings.Settings
import vadimerenkov.autasker.common.settings.enums.DateFormat
import vadimerenkov.autasker.common.settings.enums.Language
import vadimerenkov.autasker.common.settings.enums.Theme
import vadimerenkov.autasker.common.settings.enums.TimeFormat
import java.util.Locale

@Composable
fun AutaskerApp(
	settings: Settings,
	content: @Composable () -> Unit
) {
	LaunchedEffect(true) {
		val savedLocale = settings.getLocale()
		if (savedLocale == null) {
			val defaultLocale = Locale.getDefault()
			if (defaultLocale.toLanguageTag() == "en-US") {
				settings.saveTimeFormat(TimeFormat.CLOCK_12)
				settings.saveDateFormat(DateFormat.MMDDYYYY)
				settings.saveFirstDayOfWeek(7)
			}
			Language.entries.forEach { language ->
				if (language.code == defaultLocale.toLanguageTag()) {
					settings.saveLanguage(language)
				}
			}
			if (!Language.entries.any { it.code == defaultLocale.toLanguageTag() }) {
				settings.saveLanguage(Language.ENGLISH)
			}
		}
	}
	val language by remember { derivedStateOf { settings.state.language } }
	val locale: Locale? = if (language == null) {
		Locale.getDefault()
	} else {
		Locale.forLanguageTag(language!!.code)
	}
	Locale.setDefault(locale)
	val LocalLanguage = staticCompositionLocalOf { locale }

	val theme by remember { derivedStateOf { settings.state.theme } }
	val isDarkTheme = if (theme == Theme.DEVICE) isSystemInDarkTheme() else theme == Theme.DARK

	val color by remember { derivedStateOf { settings.state.themeColor} }

	AutaskerTheme(
		themeColor = color,
		darkTheme = isDarkTheme
	) {
		CompositionLocalProvider(LocalLanguage provides locale) {
			content()
		}
	}
}