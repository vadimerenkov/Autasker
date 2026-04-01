package vadimerenkov.autasker.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import vadimerenkov.autasker.data.AppDatabase
import vadimerenkov.autasker.domain.reminders.AlarmReceiver
import vadimerenkov.autasker.domain.reminders.ReminderService
import vadimerenkov.autasker.presentation.main.AudioPlayer
import vadimerenkov.autasker.settings.createAndroidDataStore

actual val platformModule = module {
	singleOf(::ReminderService)
	singleOf(::AlarmReceiver)

	single {
		AudioPlayer(androidContext())
	}

	single {
		createAndroidDataStore(androidContext())
	}

	single {
		AppDatabase(androidContext())
	}
}