package vadimerenkov.autasker.data.mappers

import vadimerenkov.autasker.data.CategoryData
import vadimerenkov.autasker.data.PageData
import vadimerenkov.autasker.data.ReminderData
import vadimerenkov.autasker.data.SubtaskData
import vadimerenkov.autasker.data.TaskData
import vadimerenkov.autasker.domain.Page
import vadimerenkov.autasker.domain.Period
import vadimerenkov.autasker.domain.Subtask
import vadimerenkov.autasker.domain.Task
import vadimerenkov.autasker.domain.TaskCategory
import vadimerenkov.autasker.domain.reminders.Reminder
import vadimerenkov.autasker.domain.toZonedDateTime
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