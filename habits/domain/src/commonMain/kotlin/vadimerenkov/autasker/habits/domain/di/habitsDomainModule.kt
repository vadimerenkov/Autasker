package vadimerenkov.autasker.habits.domain.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import vadimerenkov.autasker.habits.domain.HabitTracker

val habitsDomainModule = module {
	singleOf(::HabitTracker)
}