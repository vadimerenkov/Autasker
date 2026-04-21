package vadimerenkov.autasker.common.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.common.data.AppDatabase
import vadimerenkov.autasker.common.data.RoomLocalRepository
import vadimerenkov.autasker.common.data.TasksDatabase
import vadimerenkov.autasker.common.domain.TasksRepository
import vadimerenkov.autasker.common.presentation.bin.BinViewModel
import vadimerenkov.autasker.common.presentation.main.MainViewModel
import vadimerenkov.autasker.common.presentation.new_day.NewDayViewModel
import vadimerenkov.autasker.common.presentation.task_edit.TaskEditViewModel
import vadimerenkov.autasker.common.settings.Settings
import vadimerenkov.autasker.common.settings.SettingsViewModel

val commonModule = module {
	viewModelOf(::MainViewModel)
	viewModelOf(::SettingsViewModel)
	viewModelOf(::BinViewModel)
	viewModelOf(::NewDayViewModel)

	viewModel<TaskEditViewModel> { params ->
		TaskEditViewModel(params[0], params[1], get(), get())
	}

	singleOf(::RoomLocalRepository).bind<TasksRepository>()

	single {
		CoroutineScope(Dispatchers.Default + SupervisorJob())
	}

	singleOf(::Settings).withOptions {
		createdAtStart()
	}

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