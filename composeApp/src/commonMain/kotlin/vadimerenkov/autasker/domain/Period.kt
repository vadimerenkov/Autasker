package vadimerenkov.autasker.domain

import androidx.compose.runtime.Composable
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.day
import autasker.composeapp.generated.resources.hour
import autasker.composeapp.generated.resources.minute
import autasker.composeapp.generated.resources.month
import autasker.composeapp.generated.resources.week
import autasker.composeapp.generated.resources.year
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.pluralStringResource

enum class Period {
	MINUTE,
	HOUR,
	DAY,
	WEEK,
	MONTH,
	YEAR
}

@Composable
fun Period.toLocalizedString(quantity: Int): String {
	return when (this) {
		Period.MINUTE -> pluralStringResource(Res.plurals.minute, quantity)
		Period.HOUR -> pluralStringResource(Res.plurals.hour, quantity)
		Period.DAY -> pluralStringResource(Res.plurals.day, quantity)
		Period.WEEK -> pluralStringResource(Res.plurals.week, quantity)
		Period.MONTH -> pluralStringResource(Res.plurals.month, quantity)
		Period.YEAR -> pluralStringResource(Res.plurals.year, quantity)
	}
}

suspend fun Period.getLocalizedString(quantity: Int): String {
	return when (this) {
		Period.MINUTE -> getPluralString(Res.plurals.minute, quantity)
		Period.HOUR -> getPluralString(Res.plurals.hour, quantity)
		Period.DAY -> getPluralString(Res.plurals.day, quantity)
		Period.WEEK -> getPluralString(Res.plurals.week, quantity)
		Period.MONTH -> getPluralString(Res.plurals.month, quantity)
		Period.YEAR -> getPluralString(Res.plurals.year, quantity)
	}
}