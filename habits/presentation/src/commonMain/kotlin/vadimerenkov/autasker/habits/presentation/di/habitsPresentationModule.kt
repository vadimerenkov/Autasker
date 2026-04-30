package vadimerenkov.autasker.habits.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import vadimerenkov.autasker.habits.presentation.HabitsViewModel

val habitsPresentationModule = module {
	viewModelOf(::HabitsViewModel)
}