package vadimerenkov.autasker.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data object MainGraph: NavKey
@Serializable
data object MainScreenRoute: NavKey
@Serializable
data object CalendarRoute: NavKey

@Serializable
data object BinScreenRoute: NavKey

@Serializable
data class EditGraph(
	@Contextual val id: Long?,
	val categoryId: Long? = null
): NavKey
@Serializable
data object TaskEditRoute: NavKey
@Serializable
data object DateTimeRoute: NavKey

@Serializable
data object SettingsRoute: NavKey
@Serializable
data object AboutRoute: NavKey
@Serializable
data object NewDayRoute: NavKey

@Serializable
data object HabitsRoute: NavKey