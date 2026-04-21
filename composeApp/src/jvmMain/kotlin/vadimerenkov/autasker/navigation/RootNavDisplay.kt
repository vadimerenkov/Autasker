package vadimerenkov.autasker.navigation

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import androidx.navigation3.ui.NavDisplay
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.about
import autasker.common.generated.resources.calendar
import autasker.common.generated.resources.edit_task
import autasker.common.generated.resources.kofi_link
import autasker.common.generated.resources.settings
import autasker.common.generated.resources.support_developer
import autasker.common.generated.resources.tasks
import autasker.common.generated.resources.trash
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import vadimerenkov.autasker.calendar.CalendarScreen
import vadimerenkov.autasker.common.presentation.about.AboutScreen
import vadimerenkov.autasker.common.presentation.bin.BinScreen
import vadimerenkov.autasker.common.presentation.components.MenuAction
import vadimerenkov.autasker.common.presentation.components.TitleBar
import vadimerenkov.autasker.common.presentation.main.MainScreen
import vadimerenkov.autasker.common.presentation.main.MainViewModel
import vadimerenkov.autasker.common.presentation.new_day.NewDayScreen
import vadimerenkov.autasker.common.presentation.new_day.NewDayViewModel
import vadimerenkov.autasker.common.presentation.task_edit.TaskEditScreen
import vadimerenkov.autasker.common.presentation.task_edit.TaskEditViewModel
import vadimerenkov.autasker.common.presentation.task_edit.calendar.DateTimeScreen
import vadimerenkov.autasker.common.presentation.windows.CommonWindow
import vadimerenkov.autasker.common.settings.Settings
import vadimerenkov.autasker.common.settings.SettingsScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RootNavDisplay(
	settings: Settings
) {
	val backstack = remember { mutableStateListOf<NavKey>(MainScreenRoute) }
	LaunchedEffect(true) {
		if (settings.checkForNewDay()) {
			backstack.add(NewDayRoute)
		}
	}
	val uriHandler = LocalUriHandler.current
	val link = stringResource(Res.string.kofi_link)
	Column {
		TitleBar(
			actions = listOf(
				MenuAction(
					text = stringResource(Res.string.settings)
				) {
					backstack.add(SettingsRoute)
				},
				MenuAction(
					text = stringResource(Res.string.about)
				) {
					backstack.add(AboutRoute)
				},
				MenuAction(
					text = stringResource(Res.string.support_developer)
				) {
					uriHandler.openUri(link)
				}
//				MenuAction(
//					text = "New day (hack)"
//				) {
//					backstack.add(NewDayRoute)
//				}
			),
			modifier = Modifier
				.background(MaterialTheme.colorScheme.secondaryContainer)
		)
		HorizontalDivider()
		PermanentNavigationDrawer(
			drawerContent = {
				PermanentDrawerSheet(
					drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer,
					modifier = Modifier
						.width(IntrinsicSize.Max)
				) {
					NavigationDrawerItem(
						shape = RectangleShape,
						label = {
							Text(
								text = stringResource(Res.string.tasks),
								fontSize = 20.sp
							)
						},
						icon = {
							Icon(
								imageVector = Icons.Default.Home,
								contentDescription = null
							)
						},
						selected = backstack.lastOrNull() == MainScreenRoute,
						onClick = {
							backstack.removeAll { it != MainScreenRoute }
						}
					)
					NavigationDrawerItem(
						shape = RectangleShape,
						label = {
							Text(
								text = stringResource(Res.string.calendar),
								fontSize = 20.sp
							)
						},
						icon = {
							Icon(
								imageVector = Icons.Default.CalendarMonth,
								contentDescription = null
							)
						},
						selected = backstack.lastOrNull() == CalendarRoute,
						onClick = {
							backstack.add(CalendarRoute)
						}
					)
					Spacer(modifier = Modifier.weight(1f))
					NavigationDrawerItem(
						shape = RectangleShape,
						label = {
							Text(
								text = stringResource(Res.string.trash),
								fontSize = 20.sp
							)
						},
						icon = {
							Icon(
								imageVector = Icons.Default.Delete,
								contentDescription = null
							)
						},
						selected = backstack.lastOrNull() == BinScreenRoute,
						onClick = {
							backstack.add(BinScreenRoute)
						}
					)

				}
			}
		) {
			NavDisplay(
				backStack = backstack,
				sceneStrategies = listOf(DialogSceneStrategy()),
				entryDecorators = listOf(
					rememberSaveableStateHolderNavEntryDecorator(),
					rememberViewModelStoreNavEntryDecorator()
				),
				entryProvider = entryProvider {

					entry<MainScreenRoute> {
						val lazyState = rememberLazyListState()
						val scope = rememberCoroutineScope()
						val viewModel: MainViewModel = koinViewModel { parametersOf(backstack) }
						Box() {
							MainScreen(
								lazyState = lazyState,
								viewModel = viewModel,
								onNewTaskClick = { categoryId ->
									backstack.add(EditGraph(null, categoryId))
								},
								onTaskClick = { id ->
									backstack.add(EditGraph(id))
								},
								modifier = Modifier
									.onPointerEvent(
										eventType = PointerEventType.Scroll
									) {
										scope.launch {
											lazyState.scrollBy(it.changes.first().scrollDelta.y * 30f)
										}
									}
							)
							HorizontalScrollbar(
								adapter = ScrollbarAdapter(lazyState),
								modifier = Modifier
									.padding(16.dp)
									.align(Alignment.BottomCenter)
							)

						}
					}
					entry<CalendarRoute> {
						CalendarScreen(
							onTaskClick = {
								backstack.add(EditGraph(it))
							}
						)
					}
					entry<EditGraph>(metadata = dialog()) {
						EditNavDisplay(
							id = it.id,
							categoryId = it.categoryId,
							onCancel = {
								backstack.removeLastOrNull()
							}
						)
					}
					entry<AboutRoute>(metadata = dialog()) {
						CommonWindow(
							title = stringResource(Res.string.about),
							size = DpSize(400.dp, 400.dp),
							onCloseRequest = { backstack.remove(AboutRoute) }
						) {
							AboutScreen()
						}
					}
					entry<SettingsRoute>(metadata = dialog()) {
						CommonWindow(
							title = stringResource(Res.string.settings),
							size = DpSize(800.dp, 1000.dp),
							onCloseRequest = { backstack.remove(SettingsRoute) }
						) {
							val scrollState = rememberScrollState()
							Box {
								SettingsScreen(
									scrollState = scrollState,
									modifier = Modifier
										.padding(end = 8.dp)
								)
								VerticalScrollbar(
									adapter = ScrollbarAdapter(scrollState),
									modifier = Modifier
										.align(Alignment.CenterEnd)
								)
							}
						}
					}
					entry<BinScreenRoute> {
						BinScreen(
							modifier = Modifier
								.background(MaterialTheme.colorScheme.background)
								.padding(16.dp)
						)
					}
					entry<NewDayRoute>(metadata = dialog()) {
						val windowState = WindowState(position = WindowPosition.Aligned(Alignment.Center))
						val viewModel = koinViewModel<NewDayViewModel> { parametersOf(backstack) }
						Window(
							state = windowState,
							onCloseRequest = {
								backstack.removeLastOrNull()
							}
						) {
							NewDayScreen(
								viewModel = viewModel,
								modifier = Modifier
									.padding(16.dp)
							)
						}
					}

				}
			)
		}
	}
}

@Composable
private fun EditNavDisplay(
	id: Long?,
	categoryId: Long? = null,
	onCancel: () -> Unit,
) {
	val backstack = remember { mutableStateListOf<NavKey>(TaskEditRoute) }
	val viewModel = koinViewModel<TaskEditViewModel> {
		parametersOf(id, categoryId)
	}
	NavDisplay(
		backStack = backstack,
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator()
		),
		entryProvider = entryProvider {
			entry<TaskEditRoute> {
				CommonWindow(
					title = stringResource(Res.string.edit_task),
					size = DpSize(800.dp, 800.dp),
					onCloseRequest = onCancel
				) {
					TaskEditScreen(
						viewModel = viewModel,
						onCancel = {
							onCancel()
						},
						onCalendarOpen = {
							backstack.add(DateTimeRoute)
						}
					)
				}
			}
			entry<DateTimeRoute> {
				CommonWindow(
					title = "Select date",
					onCloseRequest = {
						backstack.remove(DateTimeRoute)
					}
				) {
					DateTimeScreen(
						dateTimeState = viewModel.dateTimeState,
						onAction = viewModel::onAction,
						onBackClick = { backstack.remove(DateTimeRoute) }
					)
				}
			}
		}
	)
}