package vadimerenkov.autasker.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import vadimerenkov.autasker.common.data.JobData
import vadimerenkov.autasker.common.domain.Page
import vadimerenkov.autasker.common.domain.Subtask
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.TaskCategory
import vadimerenkov.autasker.common.domain.TasksRepository
import vadimerenkov.autasker.common.domain.Time
import vadimerenkov.autasker.common.domain.reminders.Reminder
import java.time.ZonedDateTime

class TasksRepositoryFake: TasksRepository {
	val tasks = MutableStateFlow(emptyList<Task>())
	val categories = MutableStateFlow(listOf(
		TaskCategory(
			id = 1,
			index = 1,
			isDefault = true
		)
	))
	val subtasks = MutableStateFlow(emptyList<Subtask>())
	val reminders = MutableStateFlow(emptyList<Reminder>())
	val jobs = MutableStateFlow(emptyList<JobData>())
	val pages = MutableStateFlow(emptyList<Page>())

	override suspend fun saveTask(task: Task): Long {
		tasks.value += task
		return task.id
	}

	override suspend fun saveTasks(tasks: List<Task>) {
		this.tasks.value += tasks
	}

	override suspend fun deleteTasks(tasks: List<Task>) {
		this.tasks.value -= tasks
	}

	override suspend fun getTask(id: Long): Task {
		return tasks.value.first { it.id == id }
	}

	override suspend fun saveCategory(category: TaskCategory) {
		categories.value += category
	}

	override suspend fun saveSubtasks(subtasks: List<Subtask>) {
		this.subtasks.value += subtasks
	}

	override suspend fun deleteSubtasksForTask(id: Long) {
		val subtasks = subtasks.value.filter { it.parentTaskId == id }
		this.subtasks.value -= subtasks
	}

	override suspend fun deleteSubtasks(subtasks: List<Subtask>) {
		this.subtasks.value -= subtasks
	}

	override fun getAllTasks(): Flow<List<Task>> {
		return tasks
	}

	override suspend fun getRepeatingTasks(): List<Task> {
		return tasks.value.filter { it.repeatState.isRepeating }
	}

	override suspend fun getCompletedTasks(): List<Task> {
		return tasks.value.filter { it.isCompleted && !it.isDeleted }
	}

	override suspend fun getYesterdayUncompletedTasks(): List<Task> {
		return tasks.value.filter { it.dueDate?.isBefore(Time.todayStart()) == true }
	}

	override suspend fun getUncompletedDatelessTasks(): List<Task> {
		return tasks.value.filter { it.dueDate == null && !it.isCompleted }
	}

	override fun getDeletedTasks(): Flow<List<Task>> {
		return tasks.map { it.filter { it.isDeleted } }
	}

	override fun getAllCategories(): Flow<List<TaskCategory>> {
		return categories
			.combine(getAllTasks()) { categories, tasks ->
				categories.map { category ->
					category.copy(tasks = tasks.filter { it.categoryId == category.id })
				}
			}
	}

	override suspend fun getCategory(id: Long): TaskCategory {
		return categories.value.first { it.id == id }
	}

	override suspend fun saveCategories(categories: List<TaskCategory>) {
		this.categories.value += categories
	}

	override suspend fun getTaskCountForCategory(id: Long): Int {
		val category = categories.value.first { it.id == id }
		return category.tasks.size
	}

	override suspend fun getDefaultCategory(): TaskCategory {
		return categories.value.first()
	}

	override suspend fun deleteCategory(id: Long) {
		categories.value -= categories.value.first { it.id == id }
	}

	override suspend fun saveReminders(reminders: List<Reminder>) {
		this.reminders.value += reminders
	}

	override suspend fun getRemindersForTask(id: Long): List<Reminder> {
		return reminders.first().filter { it.parentTaskId == id }
	}

	override fun getAllReminders(): Flow<Map<Task, List<ZonedDateTime>>> {
		TODO("Not yet implemented")
	}

	override suspend fun deleteRemindersForTask(id: Long) {
		reminders.value -= reminders.value.filter { it.parentTaskId == id }
	}

	override suspend fun getAllJobs(): List<JobData> {
		return jobs.value
	}

	override suspend fun saveJob(job: JobData) {
		jobs.value += job
	}

	override suspend fun getJobsForTask(id: Long): List<JobData> {
		return jobs.value.filter { it.parentTaskId == id }
	}

	override suspend fun deleteJob(jobKey: String) {
		val job = jobs.value.first { it.key == jobKey }
		jobs.value -= job
	}

	override fun getAllPages(): Flow<List<Page>> {
		return pages
	}

	override suspend fun savePage(page: Page) {
		pages.value += page
	}

	override suspend fun deletePage(id: Long) {
		val page = pages.value.first { it.id == id }
		pages.value -= page
	}
}