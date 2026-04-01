package vadimerenkov.autasker.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.delete_tab
import autasker.composeapp.generated.resources.new_tab
import org.jetbrains.compose.resources.stringResource
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import vadimerenkov.autasker.domain.Page
import vadimerenkov.autasker.presentation.components.ButtonsRow
import vadimerenkov.autasker.presentation.main.MainState

@Composable
fun EditTabsDialog(
	state: MainState,
	onSavePagesClick: (List<Page>) -> Unit,
	onDeletePagesClick: (List<Long>) -> Unit,
	onDismissRequest: () -> Unit
) {

	Dialog(onDismissRequest = onDismissRequest) {
		var items by remember { mutableStateOf(state.pages) }
		val lazyState = rememberLazyListState()
		val reorderableState = rememberReorderableLazyListState(lazyState) { from, to ->
			items = items.toMutableList().apply {
				add(to.index, removeAt(from.index))
			}
		}
		val pagesToDelete = mutableListOf<Long>()
		LazyColumn(
			state = lazyState,
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.background(MaterialTheme.colorScheme.background)
				.padding(16.dp)
		) {
			items(
				items = items,
				key = { it.id }
			) { page ->
				ReorderableItem(
					enabled = page.id != 1L,
					state = reorderableState,
					key = page.id
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceBetween,
						modifier = Modifier.fillMaxWidth()
					) {
						Icon(
							imageVector = Icons.Default.Menu,
							contentDescription = null,
							tint = if (page.id == 1L) Color.LightGray else Color.Black,
							modifier = Modifier
								.draggableHandle(
									enabled = page.id != 1L
								)
						)
						OutlinedTextField(
							value = page.title ?: stringResource(Res.string.new_tab),
							onValueChange = {
								items = items.map { listedPage ->
									if (page.id == listedPage.id) {
										page.copy(title = it)
									} else listedPage
								}
							},
							colors = TextFieldDefaults.colors(
								unfocusedContainerColor = MaterialTheme.colorScheme.background
							),
							modifier = Modifier
								.widthIn(max = 200.dp)
						)
						IconButton(
							enabled = page.id != 1L,
							onClick = {
								items = items.toMutableList().apply { remove(page) }
								pagesToDelete.add(page.id)
							}
						) {
							Icon(
								imageVector = Icons.Default.Delete,
								contentDescription = stringResource(Res.string.delete_tab)
							)

						}
					}

				}
			}
			item {
				ButtonsRow(
					onPrimaryClick = {
						onSavePagesClick(items.map { it.copy(index = items.indexOf(it)) })
						onDeletePagesClick(pagesToDelete)
						onDismissRequest()
					},
					onSecondaryClick = onDismissRequest
				)
			}
		}
	}
}