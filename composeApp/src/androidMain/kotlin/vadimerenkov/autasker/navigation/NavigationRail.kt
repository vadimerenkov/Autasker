package vadimerenkov.autasker.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.about
import autasker.composeapp.generated.resources.calendar
import autasker.composeapp.generated.resources.settings
import autasker.composeapp.generated.resources.tasks
import autasker.composeapp.generated.resources.trash
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainNavigationRail(
	backstack: MutableList<NavKey>,
) {
	NavigationRail() {
		NavigationRailItem(
			selected = backstack.lastOrNull() == MainScreenRoute,
			onClick = {
				backstack.removeAll { it != MainScreenRoute }
			},
			icon = {
				Icon(
					imageVector = Icons.Default.Home,
					contentDescription = null
				)
			},
			label = {
				Text(
					text = stringResource(Res.string.tasks)
				)
			}
		)
		NavigationRailItem(
			selected = backstack.lastOrNull() == CalendarRoute,
			onClick = {
				if (backstack.lastOrNull() != CalendarRoute) {
					backstack.add(CalendarRoute)
				}
			},
			icon = {
				Icon(
					imageVector = Icons.Default.CalendarMonth,
					contentDescription = null
				)
			},
			label = {
				Text(
					text = stringResource(Res.string.calendar)
				)
			}
		)
		Spacer(modifier = Modifier.weight(1f))
		NavigationRailItem(
			selected = backstack.lastOrNull() == BinScreenRoute,
			onClick = {
				if (backstack.lastOrNull() != BinScreenRoute) {
					backstack.add(BinScreenRoute)
				}
			},
			icon = {
				Icon(
					imageVector = Icons.Default.Delete,
					contentDescription = null
				)
			},
			label = {
				Text(
					text = stringResource(Res.string.trash)
				)
			}
		)
		NavigationRailItem(
			selected = backstack.lastOrNull() == SettingsRoute,
			onClick = {
				if (backstack.lastOrNull() != SettingsRoute) {
					backstack.add(SettingsRoute)
				}
			},
			icon = {
				Icon(
					imageVector = Icons.Default.Settings,
					contentDescription = null
				)
			},
			label = {
				Text(
					text = stringResource(Res.string.settings)
				)
			}
		)
		NavigationRailItem(
			selected = backstack.lastOrNull() == AboutRoute,
			onClick = {
				if (backstack.lastOrNull() != AboutRoute) {
					backstack.add(AboutRoute)
				}
			},
			icon = {
				Icon(
					imageVector = Icons.Default.Info,
					contentDescription = null
				)
			},
			label = {
				Text(
					text = stringResource(Res.string.about)
				)
			}
		)
	}
}