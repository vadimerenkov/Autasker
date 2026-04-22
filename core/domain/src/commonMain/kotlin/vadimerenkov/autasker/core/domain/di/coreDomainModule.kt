package vadimerenkov.autasker.core.domain.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import vadimerenkov.autasker.core.domain.settings.Settings

val coreDomainModule = module {
	single {
		CoroutineScope(Dispatchers.Default + SupervisorJob())
	}

	singleOf(::Settings).withOptions {
		createdAtStart()
	}
}

expect val platformCoreDomainModule: Module