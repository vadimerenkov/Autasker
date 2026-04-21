package vadimerenkov.autasker.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import vadimerenkov.autasker.calendar.CalendarScreen
import vadimerenkov.autasker.common.presentation.about.AboutScreen
import vadimerenkov.autasker.common.presentation.bin.BinScreen
import vadimerenkov.autasker.common.presentation.main.MainScreen
import vadimerenkov.autasker.common.presentation.main.MainViewModel
import vadimerenkov.autasker.common.presentation.new_day.NewDayScreen
import vadimerenkov.autasker.common.presentation.new_day.NewDayViewModel
import vadimerenkov.autasker.common.presentation.task_edit.TaskEditScreen
import vadimerenkov.autasker.common.presentation.task_edit.TaskEditViewModel
import vadimerenkov.autasker.common.presentation.task_edit.calendar.DateTimeScreen
import vadimerenkov.autasker.common.settings.SettingsScreen

@Composable
fun RootNavDisplay() {
	val backstack = rememberNavBackStack(MainGraph)
	println("Recomposed root display")

	NavDisplay(
		backStack = backstack,
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator()
		),
		entryProvider = entryProvider {
			entry<MainGraph> {
				val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
				if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
					NavigationRailNavDisplay(
						mainBackStack = backstack
					)
				} else {
					FoldedDrawerNavDisplay(backstack)
				}
            }
			entry<EditGraph> {
				EditNavDisplay(
					id = it.id,
					categoryId = it.categoryId,
					onCancel = { backstack.remove(it) }
				)
			}
			entry<NewDayRoute> {
				val viewModel = koinViewModel<NewDayViewModel> { parametersOf(backstack) }
				Scaffold() { innerPadding ->
					NewDayScreen(
						viewModel = viewModel,
						modifier = Modifier
							.padding(innerPadding)
					)
				}
			}
		}
	)
}

@Composable
private fun NavigationRailNavDisplay(
	mainBackStack: MutableList<NavKey>
) {
	val backstack = rememberNavBackStack(MainScreenRoute)
	val viewModel = koinViewModel<MainViewModel> { parametersOf(mainBackStack) }

	Row(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.background)
			.systemBarsPadding()
	) {
		MainNavigationRail(backstack)
		NavDisplay(
			backStack = backstack,
			entryDecorators = listOf(
				rememberSaveableStateHolderNavEntryDecorator(),
				rememberViewModelStoreNavEntryDecorator()
			),
			entryProvider = entryProvider {
				entry<MainScreenRoute> {
					MainScreen(
						viewModel = viewModel,
						onNewTaskClick = { categoryId ->
							mainBackStack.add(EditGraph(null, categoryId))
						}
					)
				}
				entry<CalendarRoute> {
					CalendarScreen(
						onTaskClick = {
							mainBackStack.add(EditGraph(it))
						}
					)
				}
				entry<SettingsRoute> {
					SettingsScreen(
						modifier = Modifier
//							.padding(horizontal = 16.dp)
					)
				}
				entry<BinScreenRoute> {
					BinScreen(
						modifier = Modifier
							.padding(16.dp)
					)
				}
				entry<AboutRoute> {
					AboutScreen(
						modifier = Modifier
							.fillMaxSize()
							.wrapContentSize()
					)
				}
			}
		)
	}
}

@Composable
private fun FoldedDrawerNavDisplay(
	mainBackStack: MutableList<NavKey>
) {
	val backstack = rememberNavBackStack(MainScreenRoute)
	val viewModel = koinViewModel<MainViewModel> { parametersOf(mainBackStack) }

    FoldedNavigationDrawer(
	    backstack = backstack,
	) {
	    NavDisplay(
		    backStack = backstack,
		    entryDecorators = listOf(
			    rememberSaveableStateHolderNavEntryDecorator(),
			    rememberViewModelStoreNavEntryDecorator()
		    ),
		    entryProvider = entryProvider {
			    entry<MainScreenRoute> {
				    MainPager(
					    viewModel = viewModel,
						onNewTaskClick = { categoryId ->
							mainBackStack.add(EditGraph(null, categoryId))
						}
				    )
			    }
			    entry<CalendarRoute> {
				    CalendarScreen(
					    onTaskClick = {
						    mainBackStack.add(EditGraph(it))
					    }
				    )
			    }
			    entry<SettingsRoute> {
				    SettingsScreen(
					    modifier = Modifier
						    .padding(horizontal = 16.dp)
				    )
			    }
			    entry<BinScreenRoute> {
				    BinScreen(
					    modifier = Modifier
						    .padding(16.dp)
				    )
			    }
			    entry<AboutRoute> {
				    AboutScreen(
						modifier = Modifier
							.fillMaxSize()
							.wrapContentSize()
					)
			    }
		    }
	    )
    }
}

@Composable
private fun EditNavDisplay(
	id: Long?,
	categoryId: Long? = null,
	onCancel: () -> Unit,
) {
	val backstack = rememberNavBackStack(TaskEditRoute)
	val viewModel: TaskEditViewModel = koinViewModel { parametersOf(id, categoryId) }

	Scaffold() { innerPadding ->
		NavDisplay(

			backStack = backstack,
			entryDecorators = listOf(
				rememberSaveableStateHolderNavEntryDecorator(),
				rememberViewModelStoreNavEntryDecorator()
			),
			modifier = Modifier
				.padding(innerPadding),
			entryProvider = entryProvider {
				entry<TaskEditRoute> {
					TaskEditScreen(
						viewModel = viewModel,
						onCancel = onCancel,
						onCalendarOpen = { backstack.add(DateTimeRoute) },
						modifier = Modifier
							.padding(16.dp)
					)
				}
				entry<DateTimeRoute> {
					val context = LocalContext.current
					var hasPermission by remember { mutableStateOf(
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
							ContextCompat.checkSelfPermission(
								context,
								Manifest.permission.POST_NOTIFICATIONS
							) == PackageManager.PERMISSION_GRANTED
						} else true
					) }
					val permissionLauncher = rememberLauncherForActivityResult(
						ActivityResultContracts.RequestPermission()) { granted ->
						hasPermission = granted
					}
					LaunchedEffect(hasPermission) {
						if (!hasPermission) {
							permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
						}
					}
					DateTimeScreen(
						dateTimeState = viewModel.dateTimeState,
						onAction = viewModel::onAction,
						onBackClick = { backstack.remove(DateTimeRoute) },
						modifier = Modifier
							.padding(16.dp)
					)
				}
			}
		)
	}
}