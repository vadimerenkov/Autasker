package vadimerenkov.autasker.core.presentation.extensions

import androidx.compose.runtime.Composable
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.by_date_asc
import autasker.core.presentation.generated.resources.by_date_desc
import autasker.core.presentation.generated.resources.by_importance_asc
import autasker.core.presentation.generated.resources.by_importance_desc
import autasker.core.presentation.generated.resources.manual
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.Sorting

@Composable
fun Sorting.toUiText(): String {
	return when (this) {
		Sorting.BY_DATE_ASCENDING -> stringResource(Res.string.by_date_asc)
		Sorting.BY_DATE_DESCENDING -> stringResource(Res.string.by_date_desc)
		Sorting.BY_IMPORTANCE_ASCENDING -> stringResource(Res.string.by_importance_asc)
		Sorting.BY_IMPORTANCE_DESCENDING -> stringResource(Res.string.by_importance_desc)
		Sorting.MANUAL -> stringResource(Res.string.manual)
	}
}