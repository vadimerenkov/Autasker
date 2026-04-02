package vadimerenkov.autasker.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.about
import autasker.composeapp.generated.resources.settings
import autasker.composeapp.generated.resources.tasks
import autasker.composeapp.generated.resources.trash
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldedNavigationDrawer(
	backstack: NavBackStack<NavKey>,
	content: @Composable () -> Unit
) {
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	val coroutineScope = rememberCoroutineScope()
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(text = "Autasker")
				},
				navigationIcon = {
					IconButton(
						onClick = {
							coroutineScope.launch {
								if (drawerState.isClosed) {
									drawerState.open()
								} else {
									drawerState.close()

								}
							}
						}
					) {
						Icon(
							imageVector = Icons.Default.Menu,
							contentDescription = null
						)
					}
				}
			)
		}
	) { innerPadding ->
		ModalNavigationDrawer(
			drawerState = drawerState,
			drawerContent = {
				ModalDrawerSheet(
					windowInsets = WindowInsets(),
					modifier = Modifier
						.widthIn(max = 300.dp)
				) {
					NavigationDrawerItem(
						shape = RectangleShape,
						label = {
							Text(
								text = stringResource(Res.string.tasks),
								fontSize = 18.sp
							)
						},
						icon = {
							Icon(
								imageVector = Icons.Default.Home,
								contentDescription = null
							)
						},
						selected = backstack.last() == MainScreenRoute,
						onClick = {
							coroutineScope.launch {
								backstack.removeAll { it != MainScreenRoute }
								drawerState.close()
							}
						}
					)
					Spacer(modifier = Modifier.weight(1f))
					NavigationDrawerItem(
						shape = RectangleShape,
						label = {
							Text(
								text = stringResource(Res.string.trash),
								fontSize = 18.sp
							)
						},
						icon = {
							Icon(
								imageVector = Icons.Default.Delete,
								contentDescription = null
							)
						},
						selected = backstack.last() == BinScreenRoute,
						onClick = {
							coroutineScope.launch {
								backstack.add(BinScreenRoute)
								drawerState.close()
							}
						}
					)
					NavigationDrawerItem(
						label = {
							Text(
								text = stringResource(Res.string.settings),
								fontSize = 18.sp
							)
						},
						icon = {
							Icon(
								imageVector = Icons.Default.Settings,
								contentDescription = null
							)
						},
						selected = backstack.last() == SettingsRoute,
						onClick = {
							coroutineScope.launch {
								backstack.add(SettingsRoute)
								drawerState.close()
							}
						}
					)
					NavigationDrawerItem(
						label = {
							Text(
								text = stringResource(Res.string.about),
								fontSize = 18.sp
							)
						},
						icon = {
							Icon(
								imageVector = Icons.Default.Info,
								contentDescription = null
							)
						},
						selected = backstack.last() == AboutRoute,
						onClick = {
							coroutineScope.launch {
								backstack.add(AboutRoute)
								drawerState.close()
							}
						}
					)
				}
			},
			modifier = Modifier
				.padding(innerPadding)
		) {
			content()
		}
	}
}