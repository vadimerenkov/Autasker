package vadimerenkov.autasker.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import vadimerenkov.autasker.presentation.theme.AutaskerTheme
import vadimerenkov.autasker.settings.DATE_FORMAT
import vadimerenkov.autasker.settings.FIRST_DAY_OF_WEEK
import vadimerenkov.autasker.settings.LANGUAGE
import vadimerenkov.autasker.settings.Settings
import vadimerenkov.autasker.settings.TIME_FORMAT
import vadimerenkov.autasker.settings.enums.DateFormat
import vadimerenkov.autasker.settings.enums.Language
import vadimerenkov.autasker.settings.enums.Theme
import vadimerenkov.autasker.settings.enums.TimeFormat
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
				settings.saveSetting(TIME_FORMAT, TimeFormat.CLOCK_12.name)
				settings.saveSetting(DATE_FORMAT, DateFormat.MMDDYYYY.name)
				settings.saveSetting(FIRST_DAY_OF_WEEK, 7)
			}
			Language.entries.forEach { language ->
				if (language.code == defaultLocale.toLanguageTag()) {
					settings.saveSetting(LANGUAGE, language.name)
				}
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

	AutaskerTheme(darkTheme = isDarkTheme) {
		CompositionLocalProvider(LocalLanguage provides locale) {
			content()
		}
	}
}