package vadimerenkov.autasker.core.database.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.core.database.AppDatabase
import vadimerenkov.autasker.core.database.RoomTasksRepository
import vadimerenkov.autasker.core.database.TasksDatabase
import vadimerenkov.autasker.core.domain.TasksRepository

val coreDatabaseModule = module {
	singleOf(::RoomTasksRepository).bind<TasksRepository>()

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

	single {
		get<TasksDatabase>().habitsDao()
	}
}

expect val platformCoreDatabaseModule: Module