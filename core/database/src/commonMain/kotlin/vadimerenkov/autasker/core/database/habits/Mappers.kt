package vadimerenkov.autasker.core.database.habits

import vadimerenkov.autasker.core.domain.habits.Habit
import vadimerenkov.autasker.core.domain.habits.HabitCompletion
import java.time.ZoneId

fun Habit.toData(): HabitData {
	return HabitData(
		id = id,
		title = title,
		period = period,
		times = times,
		type = type,
		customQuantifier = customQuantifier
	)
}

fun HabitData.toHabit(): Habit {
	return Habit(
		id = id,
		title = title,
		period = period,
		times = times,
		type = type,
		customQuantifier = customQuantifier
	)
}

fun HabitCompletion.toData(): HabitCompletionData {
	return HabitCompletionData(
		id = id,
		habitId = habitId,
		date = date.toInstant(),
		quantity = quantity
	)
}

fun HabitCompletionData.toCompletion(): HabitCompletion {
	return HabitCompletion(
		id = id,
		habitId = habitId,
		date = date.atZone(ZoneId.systemDefault()),
		quantity = quantity
	)
}