package vadimerenkov.autasker.common.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.common.data.AppDatabase
import vadimerenkov.autasker.common.domain.reminders.QuartzScheduler
import vadimerenkov.autasker.common.domain.reminders.ReminderService
import vadimerenkov.autasker.common.presentation.main.AudioPlayer
import vadimerenkov.autasker.common.presentation.main.ClipPlayer
import vadimerenkov.autasker.common.settings.createDesktopDataStore

actual val commonPlatformModule = module {

	singleOf(::QuartzScheduler).bind<ReminderService>()
	singleOf(::ClipPlayer).bind<AudioPlayer>()

	single {
		AppDatabase()
	}

	single {
		createDesktopDataStore()
	}
}