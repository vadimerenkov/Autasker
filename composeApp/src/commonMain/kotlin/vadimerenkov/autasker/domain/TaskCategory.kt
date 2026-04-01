package vadimerenkov.autasker.domain

import androidx.compose.runtime.Composable
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.by_date_asc
import autasker.composeapp.generated.resources.by_date_desc
import autasker.composeapp.generated.resources.by_importance_asc
import autasker.composeapp.generated.resources.by_importance_desc
import autasker.composeapp.generated.resources.manual
import org.jetbrains.compose.resources.stringResource

data class TaskCategory(
	val id: Long = 0,
	val title: String? = null,
	val tasks: List<Task> = emptyList(),
	val sorting: Sorting = Sorting.MANUAL,
	val isDefault: Boolean = false,
	val isEditable: Boolean = true,
	val isDeleted: Boolean = false,
	val index: Int,
	val completedOpen: Boolean = true,
	val pageId: Long = 1,
) {
	val canDelete: Boolean
		get() = !isDefault && isEditable
}

enum class Sorting {
	BY_DATE_ASCENDING,
	BY_DATE_DESCENDING,
	BY_IMPORTANCE_ASCENDING,
	BY_IMPORTANCE_DESCENDING,
	MANUAL
}

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