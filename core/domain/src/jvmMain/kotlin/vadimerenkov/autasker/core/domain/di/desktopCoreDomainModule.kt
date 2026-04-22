package vadimerenkov.autasker.core.domain.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.core.domain.reminders.QuartzScheduler
import vadimerenkov.autasker.core.domain.reminders.ReminderService
import vadimerenkov.autasker.core.domain.settings.createDesktopDataStore

actual val platformCoreDomainModule = module {
	singleOf(::QuartzScheduler).bind<ReminderService>()

	single {
		createDesktopDataStore()
	}
}