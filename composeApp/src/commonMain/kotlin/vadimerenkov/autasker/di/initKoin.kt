package vadimerenkov.autasker.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import vadimerenkov.autasker.calendar.calendarModule
import vadimerenkov.autasker.core.database.di.coreDatabaseModule
import vadimerenkov.autasker.core.domain.di.coreDomainModule
import vadimerenkov.autasker.core.presentation.di.corePresentationModule
import vadimerenkov.autasker.habits.data.di.habitsDataModule
import vadimerenkov.autasker.habits.domain.di.habitsDomainModule
import vadimerenkov.autasker.habits.presentation.di.habitsPresentationModule

fun initKoin(config: KoinAppDeclaration? = null) {
	startKoin {
		config?.invoke(this)
		modules(
			coreDatabaseModule,
			coreDomainModule,
			corePresentationModule,
			calendarModule,
			habitsDomainModule,
			habitsDataModule,
			habitsPresentationModule
		)
	}
}