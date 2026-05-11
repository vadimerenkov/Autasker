package vadimerenkov.autasker.habits.presentation.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vadimerenkov.autasker.habits.presentation.HabitsViewModel
import vadimerenkov.autasker.habits.presentation.edit.HabitEditViewModel

val habitsPresentationModule = module {
	viewModelOf(::HabitsViewModel)

	viewModel<HabitEditViewModel> { params ->
		HabitEditViewModel(params[0], get())
	}
}