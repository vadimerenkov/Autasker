package vadimerenkov.autasker.core.domain.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import vadimerenkov.autasker.core.domain.settings.createAndroidDataStore

actual val platformCoreDomainModule = module {

	single {
		createAndroidDataStore(androidContext())
	}
}