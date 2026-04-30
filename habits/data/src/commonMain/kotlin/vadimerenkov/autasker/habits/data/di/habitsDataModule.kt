package vadimerenkov.autasker.habits.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import vadimerenkov.autasker.habits.data.RoomHabitsRepository
import vadimerenkov.autasker.habits.domain.HabitsRepository

val habitsDataModule = module {
	singleOf(::RoomHabitsRepository).bind<HabitsRepository>()
}