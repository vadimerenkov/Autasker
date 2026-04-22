package vadimerenkov.autasker.core.presentation.reminders

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import vadimerenkov.autasker.core.domain.TasksRepository
import vadimerenkov.autasker.core.presentation.R
import vadimerenkov.autasker.core.presentation.util.SuspendDateFormatter

class AlarmReceiver: BroadcastReceiver() {
	val repository: TasksRepository = get().get()
	val globalScope: CoroutineScope = get().get()

	override fun onReceive(context: Context?, intent: Intent?) {
		println("received alarm, context is $context, intent is $intent")
		if (context == null || intent == null) return
		val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
		globalScope.launch {
			val taskId = intent.getLongExtra("taskId", 0)
			val jobHashCode = intent.getIntExtra("jobHashCode", 0)
			val task = repository.getTask(taskId)
			val icon = Icon.createWithResource(context, R.drawable.app_icon)
			println("icon is $icon")

			println("Received alarm for task $task")
			val notification = Notification.Builder(context, REMINDER_CHANNEL)
				.setContentTitle(task.title)
				.setContentText(SuspendDateFormatter.formatDuration(task.dueDate!!, task.isAllDay))
				.setSmallIcon(icon)
				.build()

			notificationManager?.notify(jobHashCode, notification)
			println("notificationManager is $notificationManager, sent notification $notification")

			val jobs = repository.getJobsForTask(taskId)
			jobs.forEach { job ->
				if (job.hashCode() == jobHashCode) {
					repository.deleteJob(job.key)
				}
			}
		}
	}
}