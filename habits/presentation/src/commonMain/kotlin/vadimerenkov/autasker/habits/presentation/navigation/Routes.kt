package vadimerenkov.autasker.habits.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HabitListRoute: NavKey

@Serializable
data object HabitDetailRoute: NavKey

@Serializable
data class HabitEditRoute(
	val id: Long
): NavKey