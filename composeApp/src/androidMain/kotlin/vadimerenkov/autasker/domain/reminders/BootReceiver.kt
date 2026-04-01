package vadimerenkov.autasker.domain.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.koin.core.context.GlobalContext.get

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