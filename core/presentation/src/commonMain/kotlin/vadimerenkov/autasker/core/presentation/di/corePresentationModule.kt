package vadimerenkov.autasker.core.presentation.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vadimerenkov.autasker.core.presentation.bin.BinViewModel
import vadimerenkov.autasker.core.presentation.main.MainViewModel
import vadimerenkov.autasker.core.presentation.new_day.NewDayViewModel
import vadimerenkov.autasker.core.presentation.settings.SettingsViewModel
import vadimerenkov.autasker.core.presentation.task_edit.TaskEditViewModel

val corePresentationModule = module {
	viewModelOf(::MainViewModel)
	viewModelOf(::SettingsViewModel)
	viewModelOf(::BinViewModel)
	viewModelOf(::NewDayViewModel)

	viewModel<TaskEditViewModel> { params ->
		TaskEditViewModel(params[0], params[1], get(), get())
	}
}

expect val platformCorePresentationModule: Module