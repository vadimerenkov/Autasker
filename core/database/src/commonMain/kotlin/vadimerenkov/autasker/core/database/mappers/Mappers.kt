package vadimerenkov.autasker.core.database.mappers

import vadimerenkov.autasker.core.database.CategoryData
import vadimerenkov.autasker.core.database.PageData
import vadimerenkov.autasker.core.database.ReminderData
import vadimerenkov.autasker.core.database.SubtaskData
import vadimerenkov.autasker.core.database.TaskData
import vadimerenkov.autasker.core.domain.Page
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.Subtask
import vadimerenkov.autasker.core.domain.Task
import vadimerenkov.autasker.core.domain.TaskCategory
import vadimerenkov.autasker.core.domain.reminders.Reminder
import vadimerenkov.autasker.core.domain.toZonedDateTime
import java.time.LocalTime

fun Task.toData(): TaskData {
	return TaskData(
		id = id,
		categoryId = categoryId,
		index = index,
		title = title,
		description = description,
		dueDateEpochSeconds = dueDate?.toEpochSecond(),
		repeatState = repeatState,
		isCompleted = isCompleted,
		isDeleted = isDeleted,
		isAllDay = isAllDay,
		deletedDateEpochSeconds = deletedDate?.toEpochSecond(),
		completedDateEpochSeconds = completedDate?.toEpochSecond(),
		importance = importance
	)
}

fun TaskData.toTask(): Task {
	return Task(
		id = id,
		categoryId = categoryId,
		title = title,
		description = description,
		dueDate = dueDateEpochSeconds.toZonedDateTime(),
		repeatState = repeatState,
		isCompleted = isCompleted,
		completedDate = completedDateEpochSeconds.toZonedDateTime(),
		deletedDate = deletedDateEpochSeconds.toZonedDateTime(),
		isDeleted = isDeleted,
		isAllDay = isAllDay,
		importance = importance,
		index = index
	)
}

fun TaskData.toTask(subtasks: List<Subtask>): Task {
	return Task(
		id = id,
		categoryId = categoryId,
		subtasks = subtasks,
		title = title,
		description = description,
		dueDate = dueDateEpochSeconds.toZonedDateTime(),
		repeatState = repeatState,
		isCompleted = isCompleted,
		completedDate = completedDateEpochSeconds.toZonedDateTime(),
		deletedDate = deletedDateEpochSeconds.toZonedDateTime(),
		isDeleted = isDeleted,
		isAllDay = isAllDay,
		importance = importance,
		index = index
	)
}

fun CategoryData.toTaskCategory(tasks: List<Task>): TaskCategory {
	return TaskCategory(
		id = id,
		title = title,
		tasks = tasks,
		sorting = sorting,
		isDefault = isDefault,
		index = index
	)
}

fun Subtask.toData(): SubtaskData {
	return SubtaskData(
		id = id,
		parentTaskId = parentTaskId,
		title = title,
		isCompleted = isCompleted,
		index = index
	)
}

fun SubtaskData.toSubtask(): Subtask {
	return Subtask(
		id = id,
		parentTaskId = parentTaskId,
		title = title,
		isCompleted = isCompleted,
		index = index
	)
}

fun CategoryData.toTaskCategory(): TaskCategory {
	return TaskCategory(
		id = id,
		title = title,
		sorting = sorting,
		isDefault = isDefault,
		isDeleted = isDeleted,
		index = index,
		completedOpen = completedOpen,
		pageId = pageId
	)
}

fun TaskCategory.toData(): CategoryData {
	return CategoryData(
		id = id,
		title = title,
		sorting = sorting,
		isDefault = isDefault,
		isDeleted = isDeleted,
		index = index,
		completedOpen = completedOpen,
		pageId = pageId
	)
}

fun Reminder.toData(): ReminderData {
	return ReminderData(
		id = id,
		parentTaskId = parentTaskId,
		period = period.name,
		times = times,
		hour = time.hour,
		minute = time.minute
	)
}

fun ReminderData.toReminder(): Reminder {
	return Reminder(
		id = id,
		parentTaskId = parentTaskId,
		period = Period.valueOf(period),
		times = times,
		time = LocalTime.of(hour, minute)
	)
}

fun PageData.toPage(): Page {
	return Page(
		id = id,
		title = title,
		index = index,
	)
}

fun Page.toData(): PageData {
	return PageData(
		id = id,
		title = title,
		index = index
	)
}