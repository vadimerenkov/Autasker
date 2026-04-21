package vadimerenkov.autasker.common.domain.reminders

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext.get
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobExecutionContext
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import vadimerenkov.autasker.common.data.JobData
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.TasksRepository
import java.time.ZonedDateTime

class QuartzScheduler (
	private val repository: TasksRepository,
	private val applicationScope: CoroutineScope
): ReminderService {
	private val channel = Channel<Task>()
	val events = channel.receiveAsFlow()

	private val scheduler: Scheduler = StdSchedulerFactory().scheduler

	init {
		scheduler.start()
		rescheduleReminders()
	}

	override suspend fun scheduleReminder(taskId: Long, date: ZonedDateTime) {

		val reminderJob = JobBuilder
			.newJob(ReminderJob::class.java)
			.usingJobData("taskId", taskId)
			.build()


		val trigger = TriggerBuilder
			.newTrigger()
			.startAt(date.toInstant())
			.build()

		scheduler.scheduleJob(reminderJob, trigger)
		val job = JobData(
			key = reminderJob.key.toString(),
			parentTaskId = taskId,
			triggerDate = date.toInstant()
		)
		repository.saveJob(job)
		println("Saved to database job: ${job.key}")
		println("Current jobs: ${scheduler.currentlyExecutingJobs}")
	}

	override suspend fun cancelRemindersForTask(taskId: Long) {
		val jobs = repository.getJobsForTask(taskId)
		jobs.forEach { job ->
			scheduler.deleteJob(JobKey(job.key.removePrefix("DEFAULT.")))
			repository.deleteJob(job.key)
			println("deleted job $job")
		}
	}

	override fun rescheduleReminders() {
		applicationScope.launch {
			val jobs = repository.getAllJobs()
			val jobDetails = jobs.associate { job ->
				val detail = JobBuilder
					.newJob(ReminderJob::class.java)
					.withIdentity(JobKey(job.key.removePrefix("DEFAULT.")))
					.usingJobData("taskId", job.parentTaskId)
					.build()

				val trigger = TriggerBuilder
					.newTrigger()
					.startAt(job.triggerDate)
					.build()

				detail to trigger
			}

			delay(3000)
			jobDetails.forEach { (detail, trigger) ->
				scheduler.scheduleJob(detail, trigger)
				println("Initiated job details: ${detail.key}, ${trigger.startTime}")
				println(scheduler.checkExists(detail.key))

			}
			println(scheduler.currentlyExecutingJobs)
		}
	}

	fun sendNotification(taskId: Long, jobKey: JobKey) {
		applicationScope.launch {
			val task = repository.getTask(taskId)
			channel.send(task)
			repository.deleteJob(jobKey.toString())
		}
	}

	class ReminderJob: Job {
		val reminderService: QuartzScheduler = get().get()

		override fun execute(context: JobExecutionContext?) {
			context?.let {
				val jobKey = it.jobDetail.key
				val taskId = it.jobDetail.jobDataMap.getLong("taskId")
				reminderService.sendNotification(taskId, jobKey)
			}

			println("Reminder went off!")
			println("Job key was ${context?.jobDetail?.key}")
			println("Task id was ${context?.jobDetail?.jobDataMap?.getLong("taskId")}")
			println("Trigger key was ${context?.trigger?.key}")
		}
	}
}

