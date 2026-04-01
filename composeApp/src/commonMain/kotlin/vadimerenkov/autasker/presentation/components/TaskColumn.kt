package vadimerenkov.autasker.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.add_title
import autasker.composeapp.generated.resources.hide_completed
import autasker.composeapp.generated.resources.show_completed
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import vadimerenkov.autasker.di.appModule
import vadimerenkov.autasker.di.platformModule
import vadimerenkov.autasker.domain.Sorting
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.domain.TaskCategory
import vadimerenkov.autasker.presentation.components.context_menu.ColumnContextMenu
import vadimerenkov.autasker.presentation.main.MainAction
import vadimerenkov.autasker.presentation.theme.AutaskerTheme
import vadimerenkov.autasker.settings.Settings
import vadimerenkov.autasker.settings.SettingsState
import vadimerenkov.autasker.settings.enums.DateFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskColumn(
	category: TaskCategory,
	onNewTaskPress: () -> Unit,
	onTaskAction: (action: MainAction) -> Unit,
	modifier: Modifier = Modifier,
	date: LocalDate? = null,
	showNewTaskButton: Boolean = true,
	flavorText: String? = null
) {
	var isMenuOpen by remember { mutableStateOf(false) }
	var isMoveDialogOpen by remember { mutableStateOf(false) }
	var width by remember { mutableStateOf(0.dp) }
	val density = LocalDensity.current

	var title by remember { mutableStateOf(category.title ?: "") }

	LaunchedEffect(category.title) {
		title = category.title ?: ""
	}

	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
			.fillMaxSize()
			.onSizeChanged {
				with (density) {
					width = it.width.toDp()
				}
			}
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier
					.border(
						width = 2.dp,
						color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
						shape = RoundedCornerShape(12.dp)
					)
					.clip(RoundedCornerShape(12.dp))
					.background(MaterialTheme.colorScheme.background)
					.width(350.dp)

		) {
			if (showNewTaskButton) {
				IconButton(
					onClick = onNewTaskPress,
					modifier = Modifier
						//.weight(1f)
						.minimumInteractiveComponentSize()
				) {
					Icon(
						imageVector = Icons.Default.Add,
						contentDescription = null,
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
			OutlinedTextField(
				value = title,
				onValueChange = {
					title = it
					onTaskAction(MainAction.ChangeColumnTitle(category.id, it))
				},
				enabled = category.isEditable,
				textStyle = LocalTextStyle.current.copy(
					fontSize = 16.sp
				),
				singleLine = true,
				shape = RectangleShape,
				placeholder = {
					Text(
						text = stringResource(Res.string.add_title),
						color = Color.LightGray
					)
				},
				trailingIcon = {
					Text(
						text = category.tasks.size.toString(),
						style = MaterialTheme.typography.displaySmall,
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
						modifier = Modifier
//							.padding(horizontal = 8.dp)
					)
				},
				modifier = Modifier
					.weight(1f)
					.wrapContentSize()
			)

			IconButton(
				onClick = {
					isMenuOpen = true
				},
				modifier = Modifier
					//.weight(1f)
					.minimumInteractiveComponentSize()
			) {
				Icon(
					imageVector = Icons.Default.Settings,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.secondary
				)
				ColumnContextMenu(
					category = category,
					isMenuOpen = isMenuOpen,
					onDismissRequest = { isMenuOpen = false },
					onTaskAction = onTaskAction
				)
			}
		}

		date?.let {
			val settings: SettingsState = koinInject<Settings>().state
			val pattern = when (settings.dateFormat) {
				DateFormat.YYYYMMDD -> "EEEE, d MMM"
				DateFormat.DDMMYYYY -> "EEEE, d MMMM"
				DateFormat.MMDDYYYY -> "EEEE, MMMM d"
			}
			val format = DateTimeFormatter.ofPattern(pattern)
			Text(
				text = format.format(it),
				style = MaterialTheme.typography.displayLarge,
				color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
				modifier = Modifier
					.align(Alignment.End)
			)
		}

		flavorText?.let { text ->
			Text(
				text = text,
				style = MaterialTheme.typography.displaySmall,
				color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.padding(top = 16.dp)
			)
		}

		val lazyListState = rememberLazyListState()
		val completed = category.tasks.filter { it.isCompleted }.sortedBy { it.index }
		var notCompleted by remember { mutableStateOf(category.tasks) }

		notCompleted = when (category.sorting) {
			Sorting.BY_DATE_ASCENDING -> (category.tasks - completed).sortedBy { it.dueDate }
			Sorting.BY_DATE_DESCENDING -> (category.tasks - completed).sortedByDescending { it.dueDate }
			Sorting.BY_IMPORTANCE_ASCENDING -> (category.tasks - completed).sortedBy { it.importance }
			Sorting.BY_IMPORTANCE_DESCENDING -> (category.tasks - completed).sortedByDescending { it.importance }
			Sorting.MANUAL -> (category.tasks - completed).sortedBy { it.index }
		}
		val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
			notCompleted = notCompleted.toMutableList().apply {
				add(to.index, removeAt(from.index))
			}
			onTaskAction(MainAction.ChangeColumnSorting(category.id, Sorting.MANUAL))
		}
		val hapticFeedback = LocalHapticFeedback.current
		LazyColumn(
			state = lazyListState,
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
//				.width(width)
		) {


			if (notCompleted.isNotEmpty()) {
				items(
					items = notCompleted,
					key = {
						it.id
					}
				) { task ->
					ReorderableItem(
						state = reorderableState,
						key = task.id
					) {
						TaskItem(
							task = task,
							onAction = onTaskAction,
							modifier = Modifier
							    .longPressDraggableHandle(
								    onDragStarted = {
										hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
								    },
									onDragStopped = {
										notCompleted = notCompleted.map { task ->
											task.copy(index = notCompleted.indexOf(task))
										}
										onTaskAction(MainAction.ReorderTasks(notCompleted))
									}
								)
						)
					}
				}
			}

			if (notCompleted.isNotEmpty() && completed.isNotEmpty()) {
				item {
					ReorderableItem(
						state = reorderableState,
						key = "divider",
						enabled = false
					) {
						Row(
							horizontalArrangement = Arrangement.spacedBy(4.dp),
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.widthIn(max = 350.dp)
						) {
							CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
								IconButton(
									onClick = {
										onTaskAction(
											MainAction.ChangeColumnCompletedView(
												category.id,
												!category.completedOpen
											)
										)
									}
								) {
									Icon(
										imageVector = if (!category.completedOpen) Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
										tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
										contentDescription = if (category.completedOpen) stringResource(
											Res.string.hide_completed
										) else stringResource(Res.string.show_completed)
									)
								}
							}
							HorizontalDivider(
								thickness = 2.dp,
								modifier = Modifier
									.weight(1f)
							)
							Text(
								text = completed.size.toString(),
								color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
							)
						}
					}
				}
			}

			if (completed.isNotEmpty()) {
				items(
					items = completed,
					key = {
						it.id
					}
				) { task ->
					AnimatedVisibility(visible = category.completedOpen) {
						ReorderableItem(
							state = reorderableState,
							key = task.id,
							enabled = false
						) {

							TaskItem(
								task = task,
								onAction = onTaskAction,
								modifier = Modifier
//							.width(width)
							)
						}
					}
				}
			}
			item {
				Spacer(
					modifier = Modifier.height(100.dp)
				)
			}
		}
	}
}


@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
private fun TaskColumnPreview() {
	KoinMultiplatformApplication(koinConfiguration { modules(appModule, platformModule)} ) {
		AutaskerTheme {
			TaskColumn(
				category = TaskCategory(
					index = 0,
					tasks = listOf(
						Task(
							id = 0,
							categoryId = 1,
							index = 0,
							title = "Here is my task"
						),
						Task(
							id = 1,
							categoryId = 1,
							index = 1,
							title = "And here is another",
							description = "With the description even"
						),
						Task(
							id = 2,
							categoryId = 1,
							index = 2,
							title = "Beware of your friend, Palpatine",
							isCompleted = true
						),
						Task(
							id = 3,
							categoryId = 1,
							index = 4,
							title = "...and your pal, Friendpatine",
							isCompleted = false
						),
						Task(
							id = 4,
							categoryId = 1,
							index = 3,
							dueDate = ZonedDateTime.now(),
							title = "Forrest Day - Hyperactive"
						),
					)
				),
				onNewTaskPress = {},
				date = LocalDate.now(),
				onTaskAction = {},
				modifier = Modifier
					.padding(16.dp)
			)

		}

	}
}