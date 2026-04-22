package vadimerenkov.autasker.core.presentation.settings

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.after_completion
import autasker.core.presentation.generated.resources.after_deletion
import autasker.core.presentation.generated.resources.auto_delete_completed
import autasker.core.presentation.generated.resources.auto_delete_from_trash
import autasker.core.presentation.generated.resources.auto_launch
import autasker.core.presentation.generated.resources.close_to_tray
import autasker.core.presentation.generated.resources.date_format
import autasker.core.presentation.generated.resources.end_of_day_time
import autasker.core.presentation.generated.resources.first_day_of_week
import autasker.core.presentation.generated.resources.language
import autasker.core.presentation.generated.resources.period_in
import autasker.core.presentation.generated.resources.play_sound
import autasker.core.presentation.generated.resources.theme
import autasker.core.presentation.generated.resources.time_format
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.settings.SettingsState
import vadimerenkov.autasker.core.domain.settings.enums.DateFormat
import vadimerenkov.autasker.core.domain.settings.enums.Language
import vadimerenkov.autasker.core.domain.settings.enums.Theme
import vadimerenkov.autasker.core.domain.settings.enums.ThemeColor
import vadimerenkov.autasker.core.domain.settings.enums.TimeFormat
import vadimerenkov.autasker.core.presentation.Platform
import vadimerenkov.autasker.core.presentation.components.IntNumberInputField
import vadimerenkov.autasker.core.presentation.extensions.darkColor
import vadimerenkov.autasker.core.presentation.extensions.lightColor
import vadimerenkov.autasker.core.presentation.extensions.nameRes
import vadimerenkov.autasker.core.presentation.extensions.stringRes
import vadimerenkov.autasker.core.presentation.extensions.toLocalizedString
import vadimerenkov.autasker.core.presentation.getPlatform
import vadimerenkov.autasker.core.presentation.settings.components.CheckboxSetting
import vadimerenkov.autasker.core.presentation.settings.components.DropdownSetting
import vadimerenkov.autasker.core.presentation.theme.AutaskerTheme
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
	val state by viewModel.settings.state.collectAsStateWithLifecycle()
	SettingsScreenRoot(
		state = state,
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

			if (getPlatform() == Platform.DESKTOP) {
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
				options = DayOfWeek.entries.map {
					it.getDisplayName(
						TextStyle.FULL_STANDALONE,
						Locale.getDefault()
					)
				},
				onOptionChosen = {
					onAction(SettingsAction.FirstDayOfWeekChange(DayOfWeek.of(it + 1)))
				},
				description = stringResource(Res.string.first_day_of_week),
				chosenOption = state.firstDayOfWeek.getDisplayName(
					TextStyle.FULL_STANDALONE,
					Locale.getDefault()
				)
			)
			val languages = Language.entries.map { it.endonym }.sorted()
			DropdownSetting(
				options = languages,
				onOptionChosen = { index ->
					scope.launch {
						val language =
							Language.entries.first { it.endonym == languages[index] }
						onAction(SettingsAction.LanguageChange(language))
					}
				},
				description = stringResource(Res.string.language),
				chosenOption = state.language?.endonym ?: Language.ENGLISH.endonym
			)
			DropdownSetting(
				options = Theme.entries.map { stringResource(it.stringRes) },
				onOptionChosen = { index ->
					onAction(SettingsAction.ThemeChange(Theme.entries[index]))
				},
				description = stringResource(Res.string.theme),
				chosenOption = stringResource(state.theme.stringRes)
			)
			Row(
				horizontalArrangement = Arrangement.SpaceEvenly,
				modifier = Modifier
					.fillMaxWidth()
			) {
				ThemeColor.entries.forEach { color ->
					val isSelected = color == state.themeColor
					Column(
						horizontalAlignment = Alignment.CenterHorizontally,
						modifier = Modifier
							.clickable {
								onAction(SettingsAction.ThemeColorChange(color))
							}
							.padding(8.dp)
					){
						Box(
							modifier = Modifier
								.clip(CircleShape)
								.size(48.dp)
								.drawBehind {
									drawArc(
										color = color.lightColor,
										startAngle = 45f,
										sweepAngle = 180f,
										useCenter = true
									)
									drawArc(
										color = color.darkColor,
										startAngle = -135f,
										sweepAngle = 180f,
										useCenter = true
									)
								}
								.border(
									width = if (isSelected) 3.dp else 0.dp,
									color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
									shape = CircleShape
								)
						)
						Text(
							text = stringResource(color.nameRes),
							color = MaterialTheme.colorScheme.onBackground
						)
					}
				}
			}
		}
	}
}

@Composable
@Preview
private fun SettingsPreview() {
	AutaskerTheme {
		SettingsScreenRoot(
			state = SettingsState(language = Language.ENGLISH),
			onAction = {},
			scrollState = rememberScrollState(),
			modifier = Modifier
				.fillMaxSize()
		)
	}
}