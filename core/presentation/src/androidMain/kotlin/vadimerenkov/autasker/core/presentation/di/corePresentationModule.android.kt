package vadimerenkov.autasker.core.presentation.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.core.domain.reminders.ReminderService
import vadimerenkov.autasker.core.presentation.main.AudioPlayer
import vadimerenkov.autasker.core.presentation.main.ExoPlayer
import vadimerenkov.autasker.core.presentation.reminders.AlarmManager
import vadimerenkov.autasker.core.presentation.reminders.AlarmReceiver

actual val platformCorePresentationModule = module {
	singleOf(::AlarmManager).bind<ReminderService>()
	singleOf(::ExoPlayer).bind<AudioPlayer>()
	singleOf(::AlarmReceiver)
}