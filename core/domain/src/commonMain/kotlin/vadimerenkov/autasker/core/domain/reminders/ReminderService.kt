package vadimerenkov.autasker.core.domain.reminders

import java.time.ZonedDateTime

interface ReminderService {
	suspend fun scheduleReminder(taskId: Long, date: ZonedDateTime)
	suspend fun cancelRemindersForTask(taskId: Long)
	fun rescheduleReminders()
}