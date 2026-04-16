package vadimerenkov.autasker.settings

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.after_completion
import autasker.composeapp.generated.resources.after_deletion
import autasker.composeapp.generated.resources.auto_delete_completed
import autasker.composeapp.generated.resources.auto_delete_from_trash
import autasker.composeapp.generated.resources.auto_launch
import autasker.composeapp.generated.resources.close_to_tray
import autasker.composeapp.generated.resources.date_format
import autasker.composeapp.generated.resources.end_of_day_time
import autasker.composeapp.generated.resources.first_day_of_week
import autasker.composeapp.generated.resources.language
import autasker.composeapp.generated.resources.period_in
import autasker.composeapp.generated.resources.play_sound
import autasker.composeapp.generated.resources.theme
import autasker.composeapp.generated.resources.time_format
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.domain.Period
import vadimerenkov.autasker.domain.toLocalizedString
import vadimerenkov.autasker.getPlatform
import vadimerenkov.autasker.presentation.components.IntNumberInputField
import vadimerenkov.autasker.presentation.theme.AutaskerTheme
import vadimerenkov.autasker.settings.components.CheckboxSetting
import vadimerenkov.autasker.settings.components.DropdownSetting
import vadimerenkov.autasker.settings.enums.DateFormat
import vadimerenkov.autasker.settings.enums.Language
import vadimerenkov.autasker.settings.enums.Theme
import vadimerenkov.autasker.settings.enums.TimeFormat
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SettingsScreen(
	modifier: Modifier = Modifier,
	scrollState: ScrollState = rememberScrollState(),
	viewModel: SettingsViewModel = koinViewModel()
) {
	SettingsScreenRoot(
		state = viewModel.settings.state,
		scrollState = scrollState,
		modifier = modifier,
		onAction = viewModel::onAction
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenRoot(
	state: SettingsState,
	scrollState: ScrollState,
	onAction: (SettingsAction) -> Unit,
	modifier: Modifier = Modifier
) {
	val scope = rememberCoroutineScope()
	Box(
		modifier = modifier
			.clip(RoundedCornerShape(8.dp))
			.background(MaterialTheme.colorScheme.background)
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = modifier
				.verticalScroll(scrollState)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				var isTimePickerOpen by remember { mutableStateOf(false) }
				Text(
					text = stringResource(Res.string.end_of_day_time),
					color = MaterialTheme.colorScheme.onBackground
				)
				Spacer(modifier = Modifier.width(8.dp))
				Button(
					onClick = { isTimePickerOpen = !isTimePickerOpen}
				) {
					Text(text = state.endOfDayTime.toString())
				}
				if (isTimePickerOpen) {
					val time = rememberTimePickerState(
						initialHour = state.endOfDayTime.hour,
						initialMinute = state.endOfDayTime.minute
					)
					TimePickerDialog(
						onDismissRequest = { isTimePickerOpen = false },
						confirmButton = {
							Button(
								onClick = {
									val t = LocalTime.of(time.hour, time.minute)
									onAction(SettingsAction.EndOfDayTimeChange(t))
									isTimePickerOpen = false
								}
							) {
								Text(text = "confirm")
							}
						},
						title = {
							Text("Choose time")
						}
					) {
						TimePicker(
							state = time
						)
					}
				}
			}
			HorizontalDivider()
			FlowRow(
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				itemVerticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.clickable {
						onAction(SettingsAction.ShouldAutoDeleteCompleted(!state.autoDeleteCompleted))
					}
			) {
				Checkbox(
					checked = state.autoDeleteCompleted,
					onCheckedChange = {
						onAction(SettingsAction.ShouldAutoDeleteCompleted(it))
					}
				)
				Text(
					text = stringResource(Res.string.auto_delete_completed),
					color = MaterialTheme.colorScheme.onBackground
				)
				Text(
					text = stringResource(Res.string.period_in),
					color = MaterialTheme.colorScheme.onBackground
				)
				IntNumberInputField(
					value = state.daysUntilDeleteCompleted.toInt(),
					onValueChange = {
						onAction(SettingsAction.DeleteCompletedDaysChange(it?.toLong() ?: 0))
					},
					minNumber = 1,
					modifier = Modifier
						.widthIn(max = 100.dp)
				)
				Text(
					text = Period.DAY.toLocalizedString(state.daysUntilDeleteCompleted.toInt()),
					color = MaterialTheme.colorScheme.onBackground
				)
				Text(
					text = stringResource(Res.string.after_completion),
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			HorizontalDivider()
			FlowRow(
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				itemVerticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.clickable {
						onAction(SettingsAction.ShouldAutoDeleteFromTrash(!state.autoDeleteFromTrash))
					}
			) {
				Checkbox(
					checked = state.autoDeleteFromTrash,
					onCheckedChange = {
						onAction(SettingsAction.ShouldAutoDeleteFromTrash(it))
					}
				)
				Text(
					text = stringResource(Res.string.auto_delete_from_trash),
					color = MaterialTheme.colorScheme.onBackground
				)
				Text(
					text = stringResource(Res.string.period_in),
					color = MaterialTheme.colorScheme.onBackground
				)
				IntNumberInputField(
					value = state.daysUntilDeleteFromTrash.toInt(),
					onValueChange = {
						onAction(SettingsAction.DeleteFromTrashDaysChange(it?.toLong() ?: 0))
					},
					minNumber = 1,
					modifier = Modifier
						.widthIn(max = 100.dp)
				)
				Text(
					text = Period.DAY.toLocalizedString(state.daysUntilDeleteFromTrash.toInt()),
					color = MaterialTheme.colorScheme.onBackground
				)
				Text(
					text = stringResource(Res.string.after_deletion),
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			HorizontalDivider()
			CheckboxSetting(
				title = stringResource(Res.string.play_sound),
				isChecked = state.playSound,
				onCheckedChange = {
					onAction(SettingsAction.PlaySoundChange(it))
				}
			)

			if (getPlatform().name == "Desktop") {
				HorizontalDivider()
				CheckboxSetting(
					title = stringResource(Res.string.close_to_tray),
					isChecked = state.closeToTray,
					onCheckedChange = {
						onAction(SettingsAction.CloseToTray(it))
					}
				)
				HorizontalDivider()
				CheckboxSetting(
					title = stringResource(Res.string.auto_launch),
					isChecked = state.autoLaunch,
					onCheckedChange = {
						onAction(SettingsAction.AutoLaunch(it))
					}
				)
			}
			HorizontalDivider()
			DropdownSetting(
				options = TimeFormat.entries.map { stringResource(it.stringRes) },
				onOptionChosen = {
					onAction(SettingsAction.TimeFormatChange(TimeFormat.entries[it]))
				},
				description = stringResource(Res.string.time_format),
				chosenOption = stringResource(state.timeFormat.stringRes)
			)
			DropdownSetting(
				options = DateFormat.entries.map { stringResource(it.stringRes) },
				onOptionChosen = {
					onAction(SettingsAction.DateFormatChange(DateFormat.entries[it]))
				},
				description = stringResource(Res.string.date_format),
				chosenOption = stringResource(state.dateFormat.stringRes)
			)
			DropdownSetting(
				options = DayOfWeek.entries.map { it.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()) },
				onOptionChosen = {
					onAction(SettingsAction.FirstDayOfWeekChange(DayOfWeek.of(it + 1)))
				},
				description = stringResource(Res.string.first_day_of_week),
				chosenOption = state.firstDayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
			)
			val languages = Language.entries.map { stringResource(it.stringRes) }.sorted()
			DropdownSetting(
				options = languages,
				onOptionChosen = { index ->
					scope.launch {
						val language = Language.entries.first { getString(it.stringRes) == languages[index] }
						onAction(SettingsAction.LanguageChange(language))
					}
				},
				description = stringResource(Res.string.language),
				chosenOption = stringResource(state.language.stringRes)
			)
			DropdownSetting(
				options = Theme.entries.map { stringResource(it.stringRes) },
				onOptionChosen = { index ->
					onAction(SettingsAction.ThemeChange(Theme.entries[index]))
				},
				description = stringResource(Res.string.theme),
				chosenOption = stringResource(state.theme.stringRes)
			)
		}
	}
}

@Composable
@Preview
private fun SettingsPreview() {
	AutaskerTheme {
		SettingsScreenRoot(
			state = SettingsState(),
			onAction = {},
			scrollState = rememberScrollState(),
			modifier = Modifier
				.fillMaxSize()
		)
	}
}