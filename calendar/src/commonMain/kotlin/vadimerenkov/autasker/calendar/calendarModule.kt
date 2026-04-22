package vadimerenkov.autasker.calendar

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val calendarModule = module {
	viewModelOf(::CalendarViewModel)
}