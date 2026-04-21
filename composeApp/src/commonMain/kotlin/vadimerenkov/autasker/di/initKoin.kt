package vadimerenkov.autasker.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import vadimerenkov.autasker.calendar.calendarModule
import vadimerenkov.autasker.common.di.commonModule
import vadimerenkov.autasker.common.di.commonPlatformModule

fun initKoin(config: KoinAppDeclaration? = null) {
	startKoin {
		config?.invoke(this)
		modules(
			commonModule,
			commonPlatformModule,
			calendarModule
		)
	}
}