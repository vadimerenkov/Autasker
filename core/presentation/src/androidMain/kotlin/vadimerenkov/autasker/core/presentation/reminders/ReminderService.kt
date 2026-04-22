package vadimerenkov.autasker.core.presentation.reminders

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import vadimerenkov.autasker.common.data.JobData
import vadimerenkov.autasker.common.domain.TasksRepository
import vadimerenkov.autasker.core.domain.reminders.ReminderService
import java.time.ZonedDateTime
import java.util.UUID

@SuppressLint("MissingPermission")
class AlarmManager(
	private val context: Context,
	private val repository: TasksRepository,
	private val applicationScope: CoroutineScope
): ReminderService {
	private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
	private val packageManager = context.packageManager
	private val bootReceiver = ComponentName(context, BootReceiver::class.java)

	init {
		packageManager.setComponentEnabledSetting(
			bootReceiver,
			PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
			PackageManager.DONT_KILL_APP
		)
	}

	override suspend fun scheduleReminder(taskId: Long, date: ZonedDateTime) {
		val jobData = JobData(
			key = UUID.randomUUID().toString(),
			parentTaskId = taskId,
			triggerDate = date.toInstant()
		)

		val intent = Intent(context, AlarmReceiver::class.java).apply {
			putExtra("taskId", taskId)
			putExtra("jobHashCode", jobData.hashCode())
		}
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			jobData.hashCode(),
			intent,
			PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
		)

		alarmManager?.setExact(
			AlarmManager.RTC_WAKEUP,
			date.toEpochSecond() * 1000,
			pendingIntent
		)
		repository.saveJob(jobData)
		println("saved job: $jobData, alarmManager is $alarmManager")
	}

	override suspend fun cancelRemindersForTask(taskId: Long) {
		val jobs = repository.getJobsForTask(taskId)
		jobs.forEach { job ->

			alarmManager?.cancel(
				PendingIntent.getBroadcast(
					context,
					job.hashCode(),
					Intent(context, AlarmReceiver::class.java),
					PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
				)
			)
			repository.deleteJob(job.key)
			println("deleted job $job")
		}
	}

	override fun rescheduleReminders() {
		applicationScope.launch {
			val jobs = repository.getAllJobs()
			var i = 0
			jobs.forEach { job ->
				val intent = Intent(context, AlarmReceiver::class.java).apply {
					putExtra("taskId", job.parentTaskId)
					putExtra("jobHashCode", job.hashCode())
				}
				val pendingIntent = PendingIntent.getBroadcast(
					context,
					job.hashCode(),
					intent,
					PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
				)
				alarmManager?.setExact(
					AlarmManager.RTC_WAKEUP,
					job.triggerDate.epochSecond * 1000,
					pendingIntent
				)
				i++
			}
			Log.d("ReminderService", "Rescheduled $i jobs")
		}
	}
}