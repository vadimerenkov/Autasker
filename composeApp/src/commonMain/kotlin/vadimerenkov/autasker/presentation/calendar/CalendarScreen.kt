package vadimerenkov.autasker.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.today
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaYearMonth
import kotlinx.datetime.toKotlinDayOfWeek
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinYearMonth
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinMultiplatformApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration
import vadimerenkov.autasker.di.appModule
import vadimerenkov.autasker.di.platformModule
import vadimerenkov.autasker.domain.Time
import vadimerenkov.autasker.presentation.components.SecondaryButton
import vadimerenkov.autasker.presentation.theme.AutaskerTheme
import vadimerenkov.autasker.presentation.util.ComposableDateFormatter
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
	onTaskClick: (Long) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: CalendarViewModel = koinViewModel()
) {
	CalendarScreenRoot(
		state = viewModel.state,
		modifier = modifier,
		onAction = { action ->
			when (action) {
				is CalendarAction.OnTaskClick -> onTaskClick(action.id)
				else -> Unit
			}
			viewModel.onAction(action)
		}
	)
}

@Composable
private fun CalendarScreenRoot(
	state: CalendarState,
	onAction: (CalendarAction) -> Unit,
	modifier: Modifier = Modifier
) {
	if (state.isDayDialogOpen) {
		CalendarDayDialog(
			selectedDay = state.selectedDay!!,
			tasks = state.tasks.filter { it.dueDate?.toLocalDate() == state.selectedDay },
			onDismissRequest = {
				onAction(CalendarAction.DismissDialog)
			}
		)
	}
	val currentMonth = remember { YearMonth.now() }
	val calendarState = rememberCalendarState(
		startMonth = currentMonth.minusYears(50).toKotlinYearMonth(),
		endMonth = currentMonth.plusYears(50).toKotlinYearMonth(),
		firstVisibleMonth = YearMonth.now().toKotlinYearMonth(),
		firstDayOfWeek = state.firstDayOfWeek.toKotlinDayOfWeek()
	)
	val scope = rememberCoroutineScope()
	VerticalCalendar(
		modifier = modifier
			.background(MaterialTheme.colorScheme.background)
			.padding(16.dp)
			.fillMaxSize(),
		state = calendarState,
		calendarScrollPaged = true,
		monthHeader = {
			val month = it.yearMonth.toJavaYearMonth().month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
			val year = it.yearMonth.year.toString()
			Column(
				modifier = Modifier
					.padding(vertical = 8.dp)
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween,
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						text = "$month $year",
						style = MaterialTheme.typography.displayLarge,
						modifier = Modifier
							.padding(start = 16.dp)
					)
					if (it.yearMonth != kotlinx.datetime.YearMonth.now()) {
						SecondaryButton(
							onClick = {
								scope.launch {
									calendarState.animateScrollToMonth(kotlinx.datetime.YearMonth.now())
								}
							},
							text = stringResource(Res.string.today)
						)
					}
				}
				WeekdaysRow(
					state = state
				)
			}
		},
		contentHeightMode = ContentHeightMode.Fill,
		dayContent = {
			DayOfMonthItem(
				day = it,
				state = state,
				onAction = onAction
			)
		}
	)
}

@Composable
private fun DayOfMonthItem(
	day: CalendarDay,
	state: CalendarState,
	onAction: (CalendarAction) -> Unit
) {
	val isGrayDay = day.position != DayPosition.MonthDate
	val isToday = day.date == Time.today().toKotlinLocalDate()
	Column(
		verticalArrangement = Arrangement.spacedBy(2.dp),
		modifier = Modifier
			.border(
				width = if (isToday) 4.dp else 0.5.dp,
				color = if (isToday) Color.Red else Color.LightGray
			)
			.clickable {
				onAction(CalendarAction.OnDaySelected(day.date.toJavaLocalDate()))
			}
			.fillMaxSize()
	) {
		Text(
			text = day.date.day.toString(),
			modifier = Modifier.padding(top = 8.dp, start = 8.dp),
			color = if (isGrayDay) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary
		)
		val thisDayTasks = state.tasks
			.filter { task ->
				task.dueDate?.toLocalDate() == day.date.toJavaLocalDate()
			}
			.sortedBy { it.dueDate }

		thisDayTasks.forEach { task ->
			Box(
				modifier = Modifier
					.background(MaterialTheme.colorScheme.primary)
					.fillMaxWidth()
					.clickable {
						onAction(CalendarAction.OnTaskClick(task.id))
					}
			) {
				val title = if (task.dueDate == null || task.isAllDay) {
					task.title
				} else {
					val time = ComposableDateFormatter.formatTime(task.dueDate.toLocalTime())
					"$time ${task.title}"
				}
				Text(
					text = title,
					color = MaterialTheme.colorScheme.onPrimary,
					maxLines = 2,
					modifier = Modifier
						.padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
				)

			}
		}
	}
}

@Composable
private fun WeekdaysRow(
	state: CalendarState,
	modifier: Modifier = Modifier
) {
	val weekdays = daysOfWeek(firstDayOfWeek = state.firstDayOfWeek.toKotlinDayOfWeek()).map {
		it.toJavaDayOfWeek()
	}

	Row(
		horizontalArrangement = Arrangement.SpaceAround,
		modifier = modifier
			.fillMaxWidth()
			.padding(top = 8.dp)
	) {
		weekdays.forEach { weekday ->
			Text(
				text = weekday.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
				fontWeight = FontWeight.Bold,
				color = if (weekday.value > 5) Color.Red else MaterialTheme.colorScheme.onBackground
			)
		}
	}
}

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
private fun CalendarPreview() {
	KoinMultiplatformApplication(config = koinConfiguration { modules(appModule, platformModule) }) {
		AutaskerTheme {
			CalendarScreenRoot(
				state = CalendarState(),
				onAction = {}
			)
		}

	}
}