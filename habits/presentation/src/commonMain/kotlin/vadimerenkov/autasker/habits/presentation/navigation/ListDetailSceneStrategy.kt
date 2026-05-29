package vadimerenkov.autasker.habits.presentation.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

class ListDetailSceneStrategy<T: Any>(
	val windowSizeClass: WindowSizeClass
): SceneStrategy<T> {

	override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
		if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
			return null
		}

		val detailEntry = entries
			.lastOrNull()
			?.takeIf { it.metadata.containsKey(ListDetailScene.DETAIL_KEY) }
			?: return null

		val listEntry = entries
			.findLast { it.metadata.containsKey(ListDetailScene.LIST_KEY) }
			?: return null

		return ListDetailScene(
			list = listEntry,
			detail = detailEntry,
			key = listEntry.contentKey,
			previousEntries = entries.dropLast(1)
		)
	}
}

@Composable
fun <T: Any> rememberListDetailSceneStrategy(): ListDetailSceneStrategy<T> {
	val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
	return remember(windowSizeClass) {
		ListDetailSceneStrategy(windowSizeClass)
	}
}