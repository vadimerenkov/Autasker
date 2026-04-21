package vadimerenkov.autasker.common.presentation.main.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.delete_tab
import autasker.composeapp.generated.resources.new_tab
import autasker.composeapp.generated.resources.rename_tab
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.domain.Page
import vadimerenkov.autasker.presentation.main.MainAction
import vadimerenkov.autasker.presentation.main.MainState

@Composable
actual fun PageTab(
	page: Page,
	state: MainState,
	onTaskAction: (MainAction) -> Unit
) {
	var isRenaming by remember { mutableStateOf(false) }
	var menuOpen by remember { mutableStateOf(false) }
	var title by remember { mutableStateOf(page.title) }

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
					keyboardOptions = KeyboardOptions(
						imeAction = ImeAction.Done
					),
					keyboardActions = KeyboardActions(
						onDone = {
							onTaskAction(MainAction.TabRename(page.id, title?.ifBlank { null }))
							isRenaming = false
						}
					)
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
			.combinedClickable(
				onClick = {},
				onLongClick = {
					menuOpen = true
				}
			)
	)
}