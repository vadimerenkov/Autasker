package vadimerenkov.autasker.core.presentation.bin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.clear_bin
import autasker.core.presentation.generated.resources.no_items_bin
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.core.presentation.components.TaskColumn
import vadimerenkov.autasker.core.presentation.main.MainAction

@Composable
fun BinScreen(
	modifier: Modifier = Modifier,
	viewModel: BinViewModel = koinViewModel()
) {
	BinScreenRoot(
		state = viewModel.state,
		onAction = viewModel::onAction,
		onTaskAction = viewModel::onTaskAction,
		modifier = modifier
			.fillMaxSize()
	)
}

@Composable
private fun BinScreenRoot(
	state: BinState,
	onAction: (BinAction) -> Unit,
	onTaskAction: (MainAction) -> Unit,
	modifier: Modifier = Modifier,
) {
	AnimatedVisibility(
		state.tasks.isEmpty()
	) {
		Text(
			text = stringResource(Res.string.no_items_bin),
			color = Color.LightGray,
			fontSize = 32.sp,
			modifier = Modifier
				.fillMaxSize()
				.wrapContentSize()
				.padding(16.dp)
		)
	}

	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
	) {
		Button(
			enabled = state.tasks.isNotEmpty(),
			onClick = {
				onAction(BinAction.Clear)
			},
		) {
			Text(
				text = stringResource(Res.string.clear_bin)
			)
		}
		LazyRow(
			horizontalArrangement = Arrangement.spacedBy(16.dp)
		) {
			items(
				items = state.categories,
				key = { it.id }
			) { category ->
				TaskColumn(
					category = category,
					onNewTaskPress = {},
					onTaskAction = { action ->
						onTaskAction(action)
					},
					showNewTaskButton = false
				)
			}
		}
	}
}