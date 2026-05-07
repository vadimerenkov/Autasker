package vadimerenkov.autasker.habits.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene

class ListDetailScene<T: Any>(
	val list: NavEntry<T>,
	val detail: NavEntry<T>,
	override val key: Any,
	override val previousEntries: List<NavEntry<T>>
): Scene<T> {

	override val entries: List<NavEntry<T>>
		get() = listOf(list, detail)

	override val content: @Composable (() -> Unit) = {
		Row(
			modifier = Modifier
				.fillMaxSize()
		) {
			Column(
				modifier = Modifier
					.weight(1f)
			) {
				list.Content()
			}
			Column(
				modifier = Modifier
					.weight(1f)
			) {
				detail.Content()
			}
		}
	}

	companion object {
		const val LIST_KEY = "ListDetailScene-List"
		const val DETAIL_KEY = "ListDetailScene-Detail"

		fun list() = mapOf(LIST_KEY to true)
		fun detail() = mapOf(DETAIL_KEY to true)
	}
}