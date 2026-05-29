package vadimerenkov.autasker.habits.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HabitListRoute: NavKey

@Serializable
data class HabitDetailRoute(
	val id: Long
): NavKey

@Serializable
data class HabitEditRoute(
	val id: Long?
): NavKey