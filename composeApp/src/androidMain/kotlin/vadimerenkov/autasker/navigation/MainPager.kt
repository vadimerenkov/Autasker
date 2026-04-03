package vadimerenkov.autasker.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.all_completed
import autasker.composeapp.generated.resources.edit_tabs
import autasker.composeapp.generated.resources.new_column
import autasker.composeapp.generated.resources.new_tab
import autasker.composeapp.generated.resources.new_task
import autasker.composeapp.generated.resources.no_tasks
import autasker.composeapp.generated.resources.today
import autasker.composeapp.generated.resources.tomorrow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.domain.TaskCategory
import vadimerenkov.autasker.domain.Time
import vadimerenkov.autasker.presentation.components.TaskColumn
import vadimerenkov.autasker.presentation.main.MainAction
import vadimerenkov.autasker.presentation.main.MainState
import vadimerenkov.autasker.presentation.main.MainViewModel
import vadimerenkov.autasker.presentation.main.components.EditTabsDialog
import vadimerenkov.autasker.presentation.main.components.MovingColumnDialog
import vadimerenkov.autasker.presentation.main.components.MovingTaskDialog
import vadimerenkov.autasker.presentation.main.components.PageTab

@Composable
fun MainPager(
	onNewTaskClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: MainViewModel = koinViewModel()
) {
	var showMovingTaskDialog by remember { mutableStateOf(false) }
	var showMovingCategoryDialog by remember { mutableStateOf(false) }
	var showEditTabsDialog by remember { mutableStateOf(false) }
	var movingTaskId: Long by remember { mutableLongStateOf(0) }
	var movingColumnId: Long by remember { mutableLongStateOf(0) }

	MainPagerRoot(
		state = viewModel.state,
		onAction = { action ->
			when (action) {
				is MainAction.MoveTaskClick -> {
					movingTaskId = action.id
					showMovingTaskDialog = true
				}
				is MainAction.MoveColumnClick -> {
					movingColumnId = action.id
					showMovingCategoryDialog = true
				}
				is MainAction.NewTaskClick -> {
					onNewTaskClick(action.categoryId)
				}
				else -> Unit
			}
			viewModel.onAction(action)
		},
		onEditTabsClick = {
			showEditTabsDialog = true
		},
		modifier = modifier
	)


	if (showMovingTaskDialog) {
		MovingTaskDialog(
			movingTaskId = movingTaskId,
			state = viewModel.state,
			onAction = viewModel::onAction,
			onDismissRequest = {
				showMovingTaskDialog = false
				movingTaskId = 0
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

	if (showMovingCategoryDialog) {
		MovingColumnDialog(
			movingColumnId = movingColumnId,
			state = viewModel.state,
			onAction = viewModel::onAction,
			onDismissRequest = {
				showMovingCategoryDialog = false
				movingColumnId = 0
			}
		)
	}
}

@Composable
private fun MainPagerRoot(
	state: MainState,
	onAction: (MainAction) -> Unit,
	onEditTabsClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	val categories = (listOf(
		TaskCategory(
			id = -1,
			isEditable = false,
			title = stringResource(Res.string.today),
			index = 0,
			tasks = state.todayTasks,
			sorting = state.todayColumnSorting,
			completedOpen = state.todayShowCompleted
		),
		TaskCategory(
			id = -2,
			isEditable = false,
			title = stringResource(Res.string.tomorrow),
			index = 1,
			tasks = state.tomorrowTasks,
			sorting = state.tomorrowColumnSorting,
			completedOpen = state.tomorrowShowCompleted
		)
	) + state.categories).filter { it.pageId == state.pages.getOrNull(state.selectedTabIndex)?.id }

	val pagerState = rememberPagerState() { categories.size + 1 }
	val scope = rememberCoroutineScope()


	Scaffold(
		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					onAction(MainAction.NewTaskClick(categories[pagerState.currentPage].id))
				},
				modifier = Modifier
					.padding(16.dp)
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = stringResource(Res.string.new_task)
				)
			}
		},
		bottomBar = {
			PrimaryScrollableTabRow(
				selectedTabIndex = state.selectedTabIndex,
				tabs = {
					state.pages.forEach { page ->
						PageTab(
							page = page,
							state = state,
							onTaskAction = { action ->
								when (action) {
									is MainAction.OnTabClick -> {
										scope.launch {
											pagerState.scrollToPage(0)
										}
									}
									else -> Unit
								}
								onAction(action)
							}
						)
					}
					IconButton(
						onClick = {
							onAction(MainAction.NewTabClick)
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
		}
	) { innerPadding ->
		val bottomPadding = innerPadding.calculateBottomPadding()
		Box(
			modifier = modifier
				.fillMaxSize()
				.padding(bottom = bottomPadding)
		) {
			HorizontalPager(
				state = pagerState,
				key = { it },
				contentPadding = PaddingValues(horizontal = 16.dp),
				pageSpacing = 16.dp
			) { page ->
				val today = stringResource(Res.string.today)
				val tomorrow = stringResource(Res.string.tomorrow)
				when (page) {
					in 0 until categories.size -> {
						TaskColumn(
							category = categories[page],
							onNewTaskPress = { onAction(MainAction.NewTaskClick(categories[page].id)) },
							onTaskAction = onAction,
							showNewTaskButton = false,
							date = when (categories[page].id) {
								-1L -> Time.today()
								-2L -> Time.tomorrow()
								else -> null
							},
							flavorText = when (categories[page].id) {
								-1L -> {
									when {
										state.todayTasks.isEmpty() -> stringResource(Res.string.no_tasks, today.lowercase())
										state.todayTasks.all { it.isCompleted } -> stringResource(Res.string.all_completed, today.lowercase())
										else -> null
									}
								}
								-2L -> {
									when {
										state.tomorrowTasks.isEmpty() -> stringResource(Res.string.no_tasks, tomorrow.lowercase())
										state.tomorrowTasks.all { it.isCompleted } -> stringResource(Res.string.all_completed, tomorrow.lowercase())
										else -> null
									}
								}
								else -> null
							}
						)
					}

					else -> {
						OutlinedButton(
							onClick = { onAction(MainAction.NewColumnClick) },
							shape = RoundedCornerShape(12.dp),
							modifier = Modifier
								.fillMaxSize()
								.padding(horizontal = 24.dp)
								.padding(bottom = 24.dp)
								.navigationBarsPadding()
						) {
							Text(
								text = stringResource(Res.string.new_column)
							)
						}
					}
				}
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.padding(16.dp)
			) {
				repeat(categories.size) {
					val selected = it == pagerState.currentPage
					val color by animateColorAsState(if (selected) Color.Gray else Color.Transparent)
					val size by animateDpAsState(if (selected) 16.dp else 12.dp)
					Box(
						modifier = Modifier
							.size(size)
							.clip(CircleShape)
							.border(width = 2.dp, shape = CircleShape, color = Color.Gray)
							.background(color)

					)
				}
				val selected = pagerState.currentPage == categories.size
				Text(
					text = "+",
					fontSize = if (selected) 20.sp else 14.sp,
					fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
					color = Color.Gray
				)
			}
		}

	}
}

@Preview
@Composable
private fun MainPagerPreview() {
	MaterialTheme {
		MainPagerRoot(
			state = MainState(),
			onAction = {},
			onEditTabsClick = {}
		)
	}
}