package vadimerenkov.autasker.domain

import androidx.compose.runtime.Composable
import autasker.composeapp.generated.resources.Res
import autasker.composeapp.generated.resources.critical
import autasker.composeapp.generated.resources.high
import autasker.composeapp.generated.resources.normal
import autasker.composeapp.generated.resources.very_high
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.domain.reminders.Reminder
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

/*
fun Task.toSubtask(parentTask: Task): Subtask {
	return Subtask(
		title = title,
		isCompleted = isCompleted,
		parentTaskId = parentTask.id,
		index = parentTask.subtasks.size + 1
	)
}

fun Subtask.toTask(category: TaskCategory): Task {
	return Task(
		title = title,
		isCompleted = isCompleted,
		categoryId = category.id,
		index = category.tasks.size + 1
	)
}

 */

fun Long?.toZonedDateTime(): ZonedDateTime? {
	return this?.let { seconds ->
		Instant.ofEpochSecond(seconds).atZone(ZoneId.systemDefault())
	}
}

@Composable
fun Int.toImportance(): String {
	return when (this) {
		1 -> stringResource(Res.string.high)
		2 -> stringResource(Res.string.very_high)
		3 -> stringResource(Res.string.critical)
		else -> stringResource(Res.string.normal)
	}
}

fun ZonedDateTime.plusPeriod(state: RepeatState, firstDayOfWeek: DayOfWeek): ZonedDateTime {
	return when (state.period) {
		Period.MINUTE -> plusMinutes(state.times)
		Period.HOUR -> plusHours(state.times)
		Period.DAY -> plusDays(state.times)
		Period.WEEK -> {
			val last = firstDayOfWeek.plus(6)
//			println("First day of week is $first, last is $last.")
			val weekStart = with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
			val weekEnd = with(TemporalAdjusters.nextOrSame(last))

//			println("Week started on $weekStart and ended on $weekEnd.")

			val weekdays = state.weekDays.map { day ->
				with(TemporalAdjusters.next(day))
			}
			val weeksToSkip = if (weekdays.min().isAfter(weekStart) && weekdays.min().isBefore(weekEnd)) 0 else state.times - 1
			weekdays.min().plusWeeks(weeksToSkip)
		}
		Period.MONTH -> plusMonths(state.times)
		Period.YEAR -> plusYears(state.times)
	}
}

fun ZonedDateTime.minusReminder(reminder: Reminder): ZonedDateTime {

	val date = when (reminder.period) {
		Period.MINUTE -> minusMinutes(reminder.times)
		Period.HOUR -> minusHours(reminder.times)
		Period.DAY -> minusDays(reminder.times).withTime(reminder.time)
		Period.WEEK -> minusWeeks(reminder.times).withTime(reminder.time)
		Period.MONTH -> minusMonths(reminder.times).withTime(reminder.time)
		Period.YEAR -> minusYears(reminder.times).withTime(reminder.time)
	}

	return date.roundToMinutes()
}

fun ZonedDateTime.roundToMinutes(): ZonedDateTime {
	return this.truncatedTo(ChronoUnit.MINUTES)
}

fun ZonedDateTime.withTime(time: LocalTime): ZonedDateTime {
	return this.withHour(time.hour).withMinute(time.minute)
}

fun ZonedDateTime?.isRoughlyEqualTo(other: ZonedDateTime?): Boolean {
	if (other == null || this == null) return false
	return truncatedTo(ChronoUnit.MINUTES).isEqual(other.truncatedTo(ChronoUnit.MINUTES))
}

/**
 * Returns true if [element] is the only element in the collection.
 */
fun <T> Collection<T?>.isSingle(element: T? = null): Boolean {
	return contains(element) && size == 1
}

@Suppress("UNCHECKED_CAST")
/**
 * Returns a map containing only key-value pairs where value is not null.
 */
fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
	filterValues { it != null } as Map<K, V>