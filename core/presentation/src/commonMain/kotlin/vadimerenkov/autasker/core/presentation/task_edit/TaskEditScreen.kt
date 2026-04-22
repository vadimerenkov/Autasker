package vadimerenkov.autasker.core.presentation.task_edit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.add_description
import autasker.common.generated.resources.add_title
import autasker.common.generated.resources.completed_on
import autasker.common.generated.resources.description
import autasker.common.generated.resources.importance
import autasker.common.generated.resources.new_subtask
import autasker.common.generated.resources.title
import autasker.common.generated.resources.today
import autasker.common.generated.resources.tomorrow
import autasker.common.generated.resources.unnamed_column
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.common.domain.formatted
import vadimerenkov.autasker.common.domain.toImportance
import vadimerenkov.autasker.common.presentation.components.ButtonsRow
import vadimerenkov.autasker.common.presentation.components.DataChip
import vadimerenkov.autasker.common.presentation.util.ComposableDateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
	onCancel: () -> Unit,
	onCalendarOpen: () -> Unit,
	modifier: Modifier = Modifier,
	viewModel: TaskEditViewModel = koinViewModel()
) {
	val scope = rememberCoroutineScope()
	TaskEditScreenRoot(
		state = viewModel.state,
		onAction = viewModel::onAction,
		onSaveTask = {
			scope.launch {
				viewModel.saveTask()
				onCancel()
			}
		},
		onCancel = onCancel,
		onCalendarOpen = onCalendarOpen,
		modifier = modifier
	)
}

@OptIn(ExperimentalLayoutApi::class)
@ExperimentalMaterial3Api
@Composable
private fun TaskEditScreenRoot(
	state: TaskEditState,
	onAction: (TaskEditAction) -> Unit,
	onSaveTask: () -> Unit,
	onCancel: () -> Unit,
	onCalendarOpen: () -> Unit,
	modifier: Modifier = Modifier
) {
	val focusRequester = remember { FocusRequester() }

	LaunchedEffect(state.isLoading) {
		if (!state.isLoading && state.title.isBlank()) {
			focusRequester.requestFocus()
		}
	}

	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
			.fillMaxSize()
	) {
		AnimatedContent(state.isLoading) { isLoading ->
			if (isLoading) {
				CircularProgressIndicator(
					modifier = Modifier
						.fillMaxSize()
						.wrapContentSize()
				)
			} else {
				Column(
					verticalArrangement = Arrangement.spacedBy(16.dp),
					modifier = Modifier
						.verticalScroll(rememberScrollState())
						.fillMaxSize()
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						modifier = Modifier
//				.fillMaxWidth()
					) {
						Checkbox(
							checked = state.isCompleted,
							onCheckedChange = {
								onAction(TaskEditAction.CompletedToggle(it))
							}
						)
						val keyboard = LocalSoftwareKeyboardController.current
						OutlinedTextField(
							value = state.title,
							label = {
								Text(
									text = stringResource(Res.string.title),
									color = MaterialTheme.colorScheme.onBackground
								)
							},
							singleLine = true,
							keyboardOptions = KeyboardOptions(
								imeAction = ImeAction.Done
							),
							keyboardActions = KeyboardActions(
								onDone = {
									keyboard?.hide()
								}
							),
							onValueChange = {
								onAction(TaskEditAction.TitleChange(it))
							},
							placeholder = {
								Text(
									text = stringResource(Res.string.add_title)
								)
							},
							modifier = Modifier
								.weight(1f)
								.focusRequester(focusRequester)
						)
						IconButton(
							onClick = {
								onAction(TaskEditAction.CalendarToggle)
								onCalendarOpen()
							},
						) {
							Icon(
								imageVector = Icons.Default.CalendarMonth,
								contentDescription = null,
								tint = MaterialTheme.colorScheme.secondary
							)
						}
					}

					Row(
						horizontalArrangement = Arrangement.spacedBy(16.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						var expanded by remember { mutableStateOf(false) }
						Text(
							text = stringResource(Res.string.importance),
							color = MaterialTheme.colorScheme.onBackground
						)
						ExposedDropdownMenuBox(
							expanded = expanded,
							onExpandedChange = { expanded = it }
						) {
							TextField(
								value = state.importance.toImportance(),
								onValueChange = {},
								readOnly = true,
								trailingIcon = {
									Icon(
										imageVector = Icons.Default.ArrowDropDown,
										contentDescription = null
									)
								},
								modifier = Modifier
									.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
							)
							ExposedDropdownMenu(
								expanded = expanded,
								onDismissRequest = { expanded = false }
							) {
								repeat(4) { int ->
									DropdownMenuItem(
										text = {
											Text(
												text = int.toImportance()
											)
										},
										onClick = {
											onAction(TaskEditAction.ImportanceChanged(int))
											expanded = false
										}
									)
								}
							}
						}
					}

					var categoryMenuOpen by remember { mutableStateOf(false) }

					if (categoryMenuOpen) {
						Dialog(
							onDismissRequest = { categoryMenuOpen = false }
						) {
							Column(
								modifier = Modifier
									.clip(RoundedCornerShape(8.dp))
									.background(MaterialTheme.colorScheme.background)
									.padding(8.dp)
							) {
								val unnamedColumn = stringResource(Res.string.unnamed_column)
								state.categories.forEach { category ->
									Text(
										text = category.title ?: "<$unnamedColumn>",
										modifier = Modifier
											.clip(RoundedCornerShape(8.dp))
											.clickable {
												onAction(TaskEditAction.CategorySelected(category.id))
												categoryMenuOpen = false
											}
											.padding(8.dp)
									)
								}
							}
						}
					}
					FlowRow(
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						verticalArrangement = Arrangement.spacedBy(8.dp)
					) {
						val unnamed = stringResource(Res.string.unnamed_column)
						DataChip(
							text = state.tag ?: "<$unnamed>",
							showCross = false,
							onCrossClick = {},
							clickable = true,
							onClick = {
								categoryMenuOpen = true
							},
							modifier = Modifier
								.fillMaxRowHeight()
						)
						if (state.dueDate == null) {
							val today = stringResource(Res.string.today)
							val tomorrow = stringResource(Res.string.tomorrow)
							DataChip(
								text = "+ ${today.lowercase()}",
								onClick = {
									onAction(TaskEditAction.CommonDatePicked(1))
								},
								showCross = false,
								modifier = Modifier
									.fillMaxRowHeight()
							)
							DataChip(
								text = "+ ${tomorrow.lowercase()}",
								onClick = {
									onAction(TaskEditAction.CommonDatePicked(2))
								},
								showCross = false,
								modifier = Modifier
									.fillMaxRowHeight()
							)
						}
						else {
							DataChip(
								text = ComposableDateFormatter.formatDate(state.dueDate, state.isAllDay),
								onClick = {
									onAction(TaskEditAction.CalendarToggle)
									onCalendarOpen()
								},
								onCrossClick = {
									onAction(TaskEditAction.DeleteDate)
								},
								modifier = Modifier
									.fillMaxRowHeight()
							)
						}
						if (state.repeatState.isRepeating) {
							DataChip(
								text = state.repeatState.formatted(),
								onClick = {
									onAction(TaskEditAction.CalendarToggle)
									onCalendarOpen()
								},
								onCrossClick = {
									onAction(TaskEditAction.DeleteRepeating)
								},
								modifier = Modifier
									.fillMaxRowHeight()
							)
						}
						state.completedDate?.let {
							val completed = stringResource(Res.string.completed_on)
							val formatted = ComposableDateFormatter.formatDate(it, false)
							DataChip(
								text = "$completed $formatted",
								showCross = false,
								clickable = false,
								modifier = Modifier
									.fillMaxRowHeight()
							)
						}
					}

					if (state.subtasks.isNotEmpty()) {
						val progress by animateFloatAsState(state.subtasks.count { it.isCompleted } / state.subtasks.size.toFloat())
						LinearProgressIndicator(
							progress = {
								progress
							},
							drawStopIndicator = { false }
						)
						Column(
							verticalArrangement = Arrangement.spacedBy(4.dp),
							modifier = Modifier
							//				.weight(1f)
						) {
							state.subtasks.forEachIndexed { index, subtask ->
								DataChip(
									text = subtask.title,
									color = Color.Gray,
									onCrossClick = {
										onAction(TaskEditAction.DeleteSubtask(index))
									},
									onClick = {
										onAction(TaskEditAction.SubtaskToggle(index))
									},
									leadingIcon = {
										Checkbox(
											checked = subtask.isCompleted,
											onCheckedChange = {
												onAction(TaskEditAction.SubtaskToggle(index))
											},
											modifier = Modifier
												.size(32.dp)
										)
									}
								)
							}
						}
					}

					var subtaskTitle by remember { mutableStateOf("") }

					OutlinedTextField(
						value = subtaskTitle,
						onValueChange = {
							subtaskTitle = it
						},
						placeholder = {
							Text(
								text = stringResource(Res.string.new_subtask)
							)
						},
						keyboardActions = KeyboardActions {
							if (subtaskTitle.isNotBlank()) {
								onAction(TaskEditAction.SaveSubtask(subtaskTitle))
								subtaskTitle = ""
							}
						},
						keyboardOptions = KeyboardOptions.Default.copy(
							imeAction = ImeAction.Next
						),
						modifier = Modifier
							.onPreviewKeyEvent {
								when (it.key) {
									Key.Enter -> {
										if (subtaskTitle.isNotBlank()) {
											onAction(TaskEditAction.SaveSubtask(subtaskTitle))
											subtaskTitle = ""
											true
										} else false
									}
									else -> false
								}
							}
					)
					TextField(
						value = state.description ?: "",
						label = {
							Text(
								text = stringResource(Res.string.description),
								color = MaterialTheme.colorScheme.onBackground
							)
						},
						placeholder = {
							Text(
								text = stringResource(Res.string.add_description)
							)
						},
						onValueChange = {
							onAction(TaskEditAction.DescriptionChange(it))
						},
						minLines = 6,
						modifier = Modifier
							.fillMaxWidth()
					)
//		Text(text = "Category id: ${state.categoryId}")
					Spacer(modifier = Modifier.weight(1f))
					ButtonsRow(
						onPrimaryClick = onSaveTask,
						isPrimaryButtonEnabled = state.isValid,
						onSecondaryClick = onCancel
					)
				}

			}
		}
	}

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(backgroundColor = 255, showBackground = true)
@Composable
private fun TaskEditScreenPreview() {
	TaskEditScreenRoot(
		state = TaskEditState(),
		onAction = {},
		onCancel = {},
		onSaveTask = {},
		onCalendarOpen = {}
	)
}