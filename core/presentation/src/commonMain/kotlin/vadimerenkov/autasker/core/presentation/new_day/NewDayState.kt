package vadimerenkov.autasker.core.presentation.new_day

import vadimerenkov.autasker.core.domain.Task


data class NewDayState(
	val listOfTabs: List<Tab> = emptyList(),
	val currentTab: Tab? = null,
	val yesterdayTasks: List<Task> = emptyList(),
	val todayTasks: List<Task> = emptyList()
) {
	val currentTabIndex: Int
		get() = listOfTabs.indexOf(currentTab)
}
