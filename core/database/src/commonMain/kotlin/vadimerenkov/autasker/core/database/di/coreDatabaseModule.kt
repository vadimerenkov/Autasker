package vadimerenkov.autasker.core.database.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.core.database.AppDatabase
import vadimerenkov.autasker.core.database.RoomLocalRepository
import vadimerenkov.autasker.core.database.TasksDatabase
import vadimerenkov.autasker.core.domain.TasksRepository

val coreDatabaseModule = module {
	singleOf(::RoomLocalRepository).bind<TasksRepository>()

	single {
		get<AppDatabase>().initialize()
	}

	single {
		get<TasksDatabase>().subtasksDao()
	}

	single {
		get<TasksDatabase>().tasksDao()
	}

	single {
		get<TasksDatabase>().remindersDao()
	}

	single {
		get<TasksDatabase>().categoriesDao()
	}

	single {
		get<TasksDatabase>().jobsDao()
	}

	single {
		get<TasksDatabase>().pagesDao()
	}
}

expect val platformCoreDatabaseModule: Module