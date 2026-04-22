package vadimerenkov.autasker.core.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.all_completed
import autasker.common.generated.resources.edit_tabs
import autasker.common.generated.resources.new_column
import autasker.common.generated.resources.new_tab
import autasker.common.generated.resources.no_tasks
import autasker.common.generated.resources.today
import autasker.common.generated.resources.tomorrow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import vadimerenkov.autasker.common.domain.TaskCategory
import vadimerenkov.autasker.common.domain.Time
import vadimerenkov.autasker.common.presentation.components.TaskColumn
import vadimerenkov.autasker.common.presentation.main.components.EditTabsDialog
import vadimerenkov.autasker.common.presentation.main.components.MovingColumnDialog
import vadimerenkov.autasker.common.presentation.main.components.MovingTaskDialog
import vadimerenkov.autasker.common.presentation.main.components.PageTab

@Composable
fun MainScreen(
	onNewTaskClick: (Long) -> Unit,
	onTaskClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: MainViewModel = koinViewModel(),
	lazyState: LazyListState = rememberLazyListState(),
	showNewTaskButton: Boolean = true,
	spacing: Dp = 16.dp
) {
	var showMoveTaskDialog by remember { mutableStateOf(false) }
	var movingTaskId: Long by remember { mutableLongStateOf(0) }

	var showMoveColumnDialog by remember { mutableStateOf(false) }
	var movingColumnId: Long by remember { mutableLongStateOf(0) }

	var showEditTabsDialog by remember { mutableStateOf(false) }

	MainScreenRoot(
		onTaskAction = { action ->
			when (action) {
				is MainAction.MoveTaskClick -> {
					movingTaskId = action.id
					showMoveTaskDialog = true
				}
				is MainAction.MoveColumnClick -> {
					movingColumnId = action.id
					showMoveColumnDialog = true
				}
				is MainAction.NewTaskClick -> {
					onNewTaskClick(action.categoryId)
				}
				is MainAction.OnTaskClick -> {
					onTaskClick(action.id)
				}
				else -> Unit
			}
			viewModel.onAction(action)

		},
		state = viewModel.state,
		lazyState = lazyState,
		spacing = spacing,
		showNewTaskButton = showNewTaskButton,
		onEditTabsClick = {
			showEditTabsDialog = true
		},
		modifier = modifier
	)

	if (showMoveTaskDialog) {
		MovingTaskDialog(
			movingTaskId = movingTaskId,
			state = viewModel.state,
			onAction = viewModel::onAction,
			onDismissRequest = {
				showMoveTaskDialog = false
				movingTaskId = 0
			}
		)
	}

	if (showMoveColumnDialog) {
		MovingColumnDialog(
			movingColumnId = movingColumnId,
			state = viewModel.state,
			onAction = viewModel::onAction,
			onDismissRequest = {
				showMoveColumnDialog = false
				movingColumnId = 0
			}
		)
	}

	if (showEditTabsDialog) {
		EditTabsDialog(
			state = viewModel.state,
			onDismissRequest = {
				showEditTabsDialog = false
			},
			onSavePagesClick = {
				viewModel.onAction(MainAction.SavePages(it))
			},
			onDeletePagesClick = { pages ->
				pages.forEach {
					viewModel.onAction(MainAction.DeleteTabClick(it))
				}
			}
		)
	}
}

@Composable
private fun MainScreenRoot(
	state: MainState,
	onTaskAction: (action: MainAction) -> Unit,
	onEditTabsClick: () -> Unit,
	modifier: Modifier = Modifier,
	lazyState: LazyListState = rememberLazyListState(),
	showNewTaskButton: Boolean = true,
	spacing: Dp = 16.dp
) {
	var categories by remember { mutableStateOf(state.categories) }
	categories = state.categories
	val reorderableState = rememberReorderableLazyListState(lazyState) { from, to ->
		categories = categories.toMutableList().apply {
			add(to.index - 2, removeAt(from.index - 2))
		}
		val indexedCategories = categories.mapIndexed { index, category ->
			category.copy(index = index)
		}
		onTaskAction(MainAction.ChangeColumnsIndices(indexedCategories))
	}

	Column {
		PrimaryScrollableTabRow(
			selectedTabIndex = state.selectedTabIndex,
		//	backgroundColor = MaterialTheme.colorScheme.primaryContainer,
			tabs = {
				state.pages.forEach { page ->
					PageTab(
						page = page,
						state = state,
						onTaskAction = onTaskAction
					)
				}
				TextButton(
					onClick = {
						onTaskAction(MainAction.NewTabClick)
					},
					shape = RectangleShape
				) {
					Icon(
						imageVector = Icons.Default.Add,
						contentDescription = stringResource(Res.string.new_tab)
					)
				}
				IconButton(
					onClick = {
						onEditTabsClick()
					},
					shape = RectangleShape
				) {
					Icon(
						imageVector = Icons.Default.Settings,
						contentDescription = stringResource(Res.string.edit_tabs)
					)
				}
			}
		)
		LazyRow(
			contentPadding = PaddingValues(16.dp),
			horizontalArrangement = Arrangement.spacedBy(spacing),
			state = lazyState,
			modifier = modifier
				.background(MaterialTheme.colorScheme.background)
				.fillMaxSize()
		) {
			if (state.selectedTabIndex == 0) {
				item {
					val today = stringResource(Res.string.today)
					ReorderableItem(
						state = reorderableState,
						key = -1,
						enabled = false
					) {
						TaskColumn(
							category = TaskCategory(
								id = -1,
								isEditable = false,
								tasks = state.todayTasks,
								title = today,
								index = 0,
								sorting = state.todayColumnSorting,
								completedOpen = state.todayShowCompleted
							),
							onNewTaskPress = {
								onTaskAction(MainAction.NewTaskClick(-1))
							},
							date = Time.today(),
							flavorText = when {
								state.todayTasks.isEmpty() -> stringResource(
									Res.string.no_tasks,
									today.lowercase()
								)

								state.todayTasks.all { it.isCompleted } -> stringResource(
									Res.string.all_completed,
									today.lowercase()
								)

								else -> null
							},
							showNewTaskButton = showNewTaskButton,
							onTaskAction = onTaskAction,
						)
					}
				}
				item {
					val tomorrow = stringResource(Res.string.tomorrow)
					ReorderableItem(
						state = reorderableState,
						enabled = false,
						key = -2
					) {
						TaskColumn(
							category = TaskCategory(
								id = -2,
								tasks = state.tomorrowTasks,
								isEditable = false,
								title = tomorrow,
								index = 1,
								sorting = state.tomorrowColumnSorting,
								completedOpen = state.tomorrowShowCompleted
							),
							onNewTaskPress = {
								onTaskAction(MainAction.NewTaskClick(-2))
							},
							date = Time.tomorrow(),
							flavorText = when {
								state.tomorrowTasks.isEmpty() -> stringResource(
									Res.string.no_tasks,
									tomorrow.lowercase()
								)

								state.tomorrowTasks.all { it.isCompleted } -> stringResource(
									Res.string.all_completed,
									tomorrow.lowercase()
								)

								else -> null
							},
							showNewTaskButton = showNewTaskButton,
							onTaskAction = onTaskAction,
						)

					}
				}

			}
			items(
				items = categories.filter { it.pageId == state.pages[state.selectedTabIndex].id },
				key = { it.id }
			) { category ->
				ReorderableItem(
					state = reorderableState,
					key = category.id
				) {
					TaskColumn(
						category = category,
						onNewTaskPress = {
							onTaskAction(MainAction.NewTaskClick(category.id))
						},
						showNewTaskButton = showNewTaskButton,
						onTaskAction = onTaskAction,
						modifier = Modifier
							//.draggableHandle()
					)
				}
			}
			item {
				OutlinedButton(
					onClick = {
						onTaskAction(MainAction.NewColumnClick)
					},
					shape = RoundedCornerShape(8.dp)
				) {
					Text(
						text = stringResource(Res.string.new_column)
					)
				}
			}
		}
	}
}