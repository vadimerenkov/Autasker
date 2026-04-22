package vadimerenkov.autasker.core.database.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import vadimerenkov.autasker.core.database.AppDatabase

actual val platformCoreDatabaseModule = module {

	single {
		AppDatabase(androidContext())
	}
}