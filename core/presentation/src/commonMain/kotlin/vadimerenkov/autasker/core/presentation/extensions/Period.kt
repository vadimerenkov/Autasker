package vadimerenkov.autasker.core.presentation.extensions

import androidx.compose.runtime.Composable
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.every_day
import autasker.core.presentation.generated.resources.every_hour
import autasker.core.presentation.generated.resources.every_minute
import autasker.core.presentation.generated.resources.every_month
import autasker.core.presentation.generated.resources.every_week
import autasker.core.presentation.generated.resources.every_year
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.pluralStringResource
import vadimerenkov.autasker.core.domain.Period

@Composable
fun Period.toLocalizedString(quantity: Int): String {
	val string = when (this) {
		Period.MINUTE -> pluralStringResource(Res.plurals.every_minute, quantity)
		Period.HOUR -> pluralStringResource(Res.plurals.every_hour, quantity)
		Period.DAY -> pluralStringResource(Res.plurals.every_day, quantity)
		Period.WEEK -> pluralStringResource(Res.plurals.every_week, quantity)
		Period.MONTH -> pluralStringResource(Res.plurals.every_month, quantity)
		Period.YEAR -> pluralStringResource(Res.plurals.every_year, quantity)
	}
	return string.takeLastWhile { it.isLetter() }
}

@Composable
fun Period.getWordEvery(quantity: Int): String {
	val string = when (this) {
		Period.MINUTE -> pluralStringResource(Res.plurals.every_minute, quantity)
		Period.HOUR -> pluralStringResource(Res.plurals.every_hour, quantity)
		Period.DAY -> pluralStringResource(Res.plurals.every_day, quantity)
		Period.WEEK -> pluralStringResource(Res.plurals.every_week, quantity)
		Period.MONTH -> pluralStringResource(Res.plurals.every_month, quantity)
		Period.YEAR -> pluralStringResource(Res.plurals.every_year, quantity)
	}
	return string.takeWhile { it.isLetter() }
}

suspend fun Period.getLocalizedString(quantity: Int): String {
	val string = when (this) {
		Period.MINUTE -> getPluralString(Res.plurals.every_minute, quantity)
		Period.HOUR -> getPluralString(Res.plurals.every_hour, quantity)
		Period.DAY -> getPluralString(Res.plurals.every_day, quantity)
		Period.WEEK -> getPluralString(Res.plurals.every_week, quantity)
		Period.MONTH -> getPluralString(Res.plurals.every_month, quantity)
		Period.YEAR -> getPluralString(Res.plurals.every_year, quantity)
	}
	return string.takeLastWhile { it.isLetter() }
}