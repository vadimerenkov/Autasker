package vadimerenkov.autasker.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import vadimerenkov.autasker.data.AppDatabase
import vadimerenkov.autasker.domain.reminders.ReminderService
import vadimerenkov.autasker.presentation.main.AudioPlayer
import vadimerenkov.autasker.settings.createDesktopDataStore

actual val platformModule = module {

	singleOf(::ReminderService)
	singleOf(::AudioPlayer)

	single {
		AppDatabase()
	}

	single {
		createDesktopDataStore()
	}
}