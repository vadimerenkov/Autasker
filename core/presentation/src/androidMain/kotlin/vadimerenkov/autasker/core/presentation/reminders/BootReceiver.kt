package vadimerenkov.autasker.core.presentation.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import vadimerenkov.autasker.core.domain.reminders.ReminderService

class BootReceiver: BroadcastReceiver() {

	val reminderService: ReminderService = get().get()

	override fun onReceive(context: Context?, intent: Intent?) {
		Log.d("BootReceiver", "Received boot")
		intent?.action?.let {
			if (it == "android.intent.action.BOOT_COMPLETED") {
				reminderService.rescheduleReminders()
			}
		}
	}
}