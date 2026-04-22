package vadimerenkov.autasker.common.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.core.database.AppDatabase
import vadimerenkov.autasker.common.domain.reminders.AlarmManager
import vadimerenkov.autasker.common.domain.reminders.AlarmReceiver
import vadimerenkov.autasker.common.domain.reminders.ReminderService
import vadimerenkov.autasker.common.presentation.main.AudioPlayer
import vadimerenkov.autasker.common.presentation.main.ExoPlayer
import vadimerenkov.autasker.common.settings.createAndroidDataStore

actual val commonPlatformModule = module {
	singleOf(::AlarmManager).bind<ReminderService>()
	singleOf(::ExoPlayer).bind<AudioPlayer>()
	singleOf(::AlarmReceiver)

	single {
		createAndroidDataStore(androidContext())
	}

	single {
		AppDatabase(androidContext())
	}
}