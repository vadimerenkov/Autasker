package vadimerenkov.autasker.core.presentation.main.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.onClick
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.PointerButton
import autasker.common.generated.resources.Res
import autasker.common.generated.resources.delete_tab
import autasker.common.generated.resources.new_tab
import autasker.common.generated.resources.rename_tab
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.common.domain.Page
import vadimerenkov.autasker.common.presentation.main.MainAction
import vadimerenkov.autasker.common.presentation.main.MainState

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun PageTab(
	page: Page,
	state: MainState,
	onTaskAction: (MainAction) -> Unit
) {
	var isRenaming by remember { mutableStateOf(false) }
	var menuOpen by remember { mutableStateOf(false) }
	var title by remember { mutableStateOf(page.title) }
	val focusRequester = remember { FocusRequester() }

	LaunchedEffect(isRenaming) {
		if (isRenaming) {
			focusRequester.requestFocus()
		}
	}

	Tab(
		selected = state.pages.indexOf(page) == state.selectedTabIndex,
		onClick = {
			onTaskAction(MainAction.OnTabClick(state.pages.indexOf(page)))
		},
		text = {
			if (isRenaming) {
				BasicTextField(
					value = title ?: "",
					onValueChange = {
						title = it
					},
					modifier = Modifier
						.onPreviewKeyEvent {
							when (it.key) {
								Key.Enter -> {
									onTaskAction(MainAction.TabRename(page.id, title?.ifBlank { null }))
									isRenaming = false
									true
								}
								else -> false
							}
						}
						.focusRequester(focusRequester)
				)
			} else {
				Text(
					text = page.title ?: stringResource(Res.string.new_tab)
				)
			}
			DropdownMenu(
				expanded = menuOpen,
				onDismissRequest = { menuOpen = false }
			) {
				DropdownMenuItem(
					onClick = {
						isRenaming = true
						menuOpen = false
					},
					text = {
						Text(
							text = stringResource(Res.string.rename_tab)
						)
					}
				)
				DropdownMenuItem(
					enabled = page.id != 1L,
					onClick = {
						onTaskAction(MainAction.DeleteTabClick(page.id))
					},
					text = {
						Text(
							text = stringResource(Res.string.delete_tab)
						)
					}
				)
			}
		},
		modifier = Modifier
			.onClick(
				matcher = PointerMatcher.mouse(PointerButton.Secondary),
				onClick = {
					menuOpen = true
				}
			)
	)
}