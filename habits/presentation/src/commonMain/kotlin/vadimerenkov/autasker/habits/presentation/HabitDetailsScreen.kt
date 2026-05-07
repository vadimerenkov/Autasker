package vadimerenkov.autasker.habits.presentation

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import autasker.core.presentation.generated.resources.Res
import autasker.core.presentation.generated.resources.next_month
import autasker.core.presentation.generated.resources.previous_month
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.minusYears
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.launch
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minusMonth
import kotlinx.datetime.plusMonth
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaYearMonth
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HabitDetailsScreen(
	habit: Habit,
	completions: List<HabitCompletion>,
	onAction: (HabitsAction) -> Unit,
	modifier: Modifier = Modifier
) {
	val scope = rememberCoroutineScope()
	val calendarState = rememberCalendarState(
		startMonth = YearMonth.now().minusYears(50),
		endMonth = YearMonth.now(),
		firstVisibleMonth = YearMonth.now()
	)

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		Text(
			text = habit.title
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
				val completion = completions.find { it.date.toLocalDate() == day.date.toJavaLocalDate() }
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.clip(CircleShape)
						.clickable {
							if (completion == null) {
								onAction(HabitsAction.OnCalendarDayClick(day.date.toJavaLocalDate(), habit.id))
							} else {
								onAction(HabitsAction.OnCalendarDayUnclick(completion.id))
							}
						}
						.background(
							color = if (completion != null) MaterialTheme.colorScheme.primary else Color.Transparent
						)
						.size(48.dp)
						.padding(8.dp)
				) {
					Text(
						text = day.date.day.toString(),
						color = if (completion != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
					)
				}
			}
		)
	}
}