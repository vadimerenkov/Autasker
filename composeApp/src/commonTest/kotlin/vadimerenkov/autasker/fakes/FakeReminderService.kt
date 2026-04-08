package vadimerenkov.autasker.fakes

import vadimerenkov.autasker.data.JobData
import vadimerenkov.autasker.domain.TasksRepository
import vadimerenkov.autasker.domain.reminders.ReminderService
import java.time.ZonedDateTime
import java.util.UUID

class FakeReminderService(
	private val repository: TasksRepository
): ReminderService {

	override suspend fun scheduleReminder(taskId: Long, date: ZonedDateTime) {
		repository.saveJob(JobData(
			key = UUID.randomUUID().toString(),
			parentTaskId = taskId,
			triggerDate = date.toInstant()
		))
		println("Scheduled a reminder for task $taskId")
	}

	override suspend fun cancelRemindersForTask(taskId: Long) {
		repository.deleteRemindersForTask(taskId)
		val jobs = repository.getAllJobs()
		jobs.forEach { job ->
			repository.deleteJob(job.key)
		}
		println("Cancelled all reminders for task $taskId")
	}

	override fun rescheduleReminders() {
		println("Rescheduled all reminders")
	}
}