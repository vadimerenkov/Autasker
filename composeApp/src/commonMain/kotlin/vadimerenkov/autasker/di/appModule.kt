package vadimerenkov.autasker.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.data.AppDatabase
import vadimerenkov.autasker.data.RoomLocalRepository
import vadimerenkov.autasker.data.TasksDatabase
import vadimerenkov.autasker.domain.TasksRepository
import vadimerenkov.autasker.presentation.bin.BinViewModel
import vadimerenkov.autasker.presentation.calendar.CalendarViewModel
import vadimerenkov.autasker.presentation.main.MainViewModel
import vadimerenkov.autasker.presentation.new_day.NewDayViewModel
import vadimerenkov.autasker.presentation.task_edit.TaskEditViewModel
import vadimerenkov.autasker.settings.Settings
import vadimerenkov.autasker.settings.SettingsViewModel

expect val platformModule: Module

val appModule = module {

//	viewModelOf(::TaskEditViewModel)
	viewModelOf(::MainViewModel)
	viewModelOf(::SettingsViewModel)
	viewModelOf(::BinViewModel)
	viewModelOf(::NewDayViewModel)
	viewModelOf(::CalendarViewModel)

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