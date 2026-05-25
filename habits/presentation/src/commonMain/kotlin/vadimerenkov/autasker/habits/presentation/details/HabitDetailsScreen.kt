package vadimerenkov.autasker.habits.presentation.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.current_streak
import autasker.core.presentation.generated.resources.minutes
import autasker.core.presentation.generated.resources.monthly_completions
import autasker.core.presentation.generated.resources.next_month
import autasker.core.presentation.generated.resources.previous_month
import autasker.core.presentation.generated.resources.times_per
import autasker.core.presentation.generated.resources.total_completions
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.minusYears
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.launch
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minusMonth
import kotlinx.datetime.plusMonth
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaYearMonth
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.Time
import vadimerenkov.autasker.core.domain.habits.HabitType
import vadimerenkov.autasker.habits.domain.di.DatePeriod
import vadimerenkov.autasker.habits.domain.di.isIn
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HabitDetailsScreen(
	state: HabitDetailsState,
	onAction: (HabitDetailsAction) -> Unit,
	modifier: Modifier = Modifier
) {
	val scope = rememberCoroutineScope()
	val calendarState = rememberCalendarState(
		startMonth = YearMonth.now().minusYears(50),
		endMonth = YearMonth.now(),
		firstVisibleMonth = YearMonth.now()
	)

	state.openedCalendarDay?.let { day ->
		OpenCalendarDayDialog(
			day = day,
			state = state,
			onAction = onAction
		)
	}

	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
	) {
		Text(
			text = state.habit.title,
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
		)
		HorizontalCalendar(
			state = calendarState,
			monthHeader = {
				val month = it.yearMonth.toJavaYearMonth().month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
				val year = it.yearMonth.year.toString()
				Row(
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
				) {
					IconButton(
						onClick = {
							scope.launch {
								calendarState.animateScrollToMonth(it.yearMonth.minusMonth())
							}
						}
					) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = stringResource(Res.string.previous_month)
						)
					}
					Text(
						text = "$month $year"
					)
					IconButton(
						onClick = {
							scope.launch {
								calendarState.animateScrollToMonth(it.yearMonth.plusMonth())
							}
						},
						enabled = it.yearMonth != YearMonth.now()
					) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowForward,
							contentDescription = stringResource(Res.string.next_month)
						)
					}
				}
			},
			dayContent = { day ->
				DayContent(
					day = day,
					state = state,
					onAction = onAction
				)
			}
		)

		val quantifier = when (state.habit.type) {
			HabitType.SINGLE -> stringResource(Res.string.times_per)
			HabitType.TIME -> stringResource(Res.string.minutes)
			HabitType.CUSTOM -> state.habit.customQuantifier ?: ""
		}

		val currentStreak = stringResource(Res.string.current_streak)
		Text(
			text = "$currentStreak ${state.currentStreak} $quantifier",
			fontSize = 24.sp
		)

		val currentMonth = calendarState.firstVisibleMonth.yearMonth.toJavaYearMonth()
		val month = DatePeriod(
			startingDate = currentMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()),
			endingDate = currentMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault())
		)
		val monthlyCompletions = state
			.completions
			.filter { it.date.isIn(month) }
			.sumOf { it.quantity }
		val monthlyCompletionsText = stringResource(Res.string.monthly_completions)
		Text(
			text = "$monthlyCompletionsText $monthlyCompletions $quantifier",
			fontSize = 24.sp
		)

		val totalCompletions = state.completions.sumOf { it.quantity }
		val totalCompletionsText = stringResource(Res.string.total_completions)
		Text(
			text = "$totalCompletionsText $totalCompletions $quantifier",
			fontSize = 24.sp
		)

		/*
		LazyColumn {

			items(
				items = state.dates
			) { period ->
				Text(text = period.toString())
			}

		}

		 */
	}
}

@Composable
private fun DayContent(
	day: CalendarDay,
	state: HabitDetailsState,
	onAction: (HabitDetailsAction) -> Unit
) {
	val dailyCompletions = state.completions.filter { it.date.toLocalDate() == day.date.toJavaLocalDate() }
	val completedPercent = if (state.habit.period == Period.DAY) dailyCompletions.size / state.habit.times.toDouble() else dailyCompletions.size.toDouble()
	val degrees = (completedPercent * 360f)
		.toFloat()
		.coerceAtMost(360f)
	val primaryColor = MaterialTheme.colorScheme.primary
	val isFutureDate = day.date.toJavaLocalDate().isAfter(Time.today())
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.clip(CircleShape)
			.clickable {
				if (!isFutureDate) {
					onAction(HabitDetailsAction.OnCalendarDayClick(day.date.toJavaLocalDate()))
				}
			}
			.drawBehind {
				drawArc(
					color = primaryColor,
					startAngle = -90f,
					sweepAngle = degrees,
					useCenter = false,
					style = Stroke(
						width = 10f,
						cap = StrokeCap.Round
					)
				)
			}
			.size(48.dp)
			.padding(8.dp)
	) {
		Text(
			text = day.date.day.toString(),
			color = if (day.position != DayPosition.MonthDate || isFutureDate) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onBackground
		)
	}
}