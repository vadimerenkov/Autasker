package vadimerenkov.autasker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import org.koin.android.ext.koin.androidContext
import vadimerenkov.autasker.composeapp.R
import vadimerenkov.autasker.di.initKoin
import vadimerenkov.autasker.domain.reminders.REMINDER_CHANNEL

class AutaskerApp: Application() {

	override fun onCreate() {
		super.onCreate()
		initKoin {
			androidContext(this@AutaskerApp)
		}

		val reminderChannel = NotificationChannel(
			REMINDER_CHANNEL,
			getString(R.string.reminders),
			NotificationManager.IMPORTANCE_HIGH
		)

		val notificationManager =
			getSystemService(NOTIFICATION_SERVICE) as? NotificationManager

		notificationManager?.createNotificationChannels(listOf(
			reminderChannel
		))
	}
}