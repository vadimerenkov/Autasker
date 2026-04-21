package vadimerenkov.autasker.common.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import vadimerenkov.autasker.common.data.dao.CategoriesDao
import vadimerenkov.autasker.common.data.dao.JobsDao
import vadimerenkov.autasker.common.data.dao.PagesDao
import vadimerenkov.autasker.common.data.dao.RemindersDao
import vadimerenkov.autasker.common.data.dao.SubtasksDao
import vadimerenkov.autasker.common.data.dao.TasksDao
import vadimerenkov.autasker.common.data.mappers.toData
import vadimerenkov.autasker.common.data.mappers.toPage
import vadimerenkov.autasker.common.data.mappers.toReminder
import vadimerenkov.autasker.common.data.mappers.toSubtask
import vadimerenkov.autasker.common.data.mappers.toTask
import vadimerenkov.autasker.common.data.mappers.toTaskCategory
import vadimerenkov.autasker.common.domain.Page
import vadimerenkov.autasker.common.domain.Subtask
import vadimerenkov.autasker.common.domain.Task
import vadimerenkov.autasker.common.domain.TaskCategory
import vadimerenkov.autasker.common.domain.TasksRepository
import vadimerenkov.autasker.common.domain.Time
import vadimerenkov.autasker.common.domain.minusReminder
import vadimerenkov.autasker.common.domain.reminders.Reminder
import vadimerenkov.autasker.common.domain.roundToMinutes
import java.time.ZonedDateTime

class RoomLocalRepository(
	private val tasksDao: TasksDao,
	private val subtasksDao: SubtasksDao,
	private val remindersDao: RemindersDao,
	private val categoriesDao: CategoriesDao,
	private val jobsDao: JobsDao,
	private val pagesDao: PagesDao
): TasksRepository {

	override fun getAllReminders(): Flow<Map<Task, List<ZonedDateTime>>> {
		return remindersDao
			.getAllReminders()
			.map { data ->
				data.map { it.toReminder() }
			}
			.map { list ->
				list.groupBy { it.parentTaskId }
			}
			.map { map ->
				map
					.mapKeys {
						getTask(it.key)
					}
					.mapValues {
						it.value.mapNotNull { reminder ->
							it.key.dueDate?.minusReminder(reminder)?.roundToMinutes()
						}
					}
			}
	}

	override suspend fun deleteRemindersForTask(id: Long) {
		remindersDao.deleteRemindersByTask(id)
	}

	override suspend fun getAllJobs(): List<JobData> {
		return jobsDao.getAllJobs()
	}

	override suspend fun saveJob(job: JobData) {
		jobsDao.saveJob(job)
	}

	override suspend fun getJobsForTask(id: Long): List<JobData> {
		return jobsDao.getJobsForTask(id)
	}

	override suspend fun deleteJob(jobKey: String) {
		jobsDao.deleteJob(jobKey)
	}

	override fun getAllPages(): Flow<List<Page>> {
		return pagesDao.getAllPages().map { it.map { it.toPage() }}
	}

	override suspend fun savePage(page: Page) {
		pagesDao.savePage(page.toData())
	}

	override suspend fun deletePage(id: Long) {
		pagesDao.deletePage(id)
	}

	override suspend fun saveTask(task: Task): Long {
		return tasksDao.upsertTask(task.toData())
	}

	override suspend fun saveTasks(tasks: List<Task>) {
		tasksDao.upsertTasks(tasks.map { it.toData() })
	}

	override suspend fun deleteTasks(tasks: List<Task>) {
		tasksDao.deleteTasks(tasks.map { it.toData() })
		tasks.forEach { task ->
			subtasksDao.deleteSubtasksByTask(task.id)
			remindersDao.deleteRemindersByTask(task.id)
		}
	}

	override suspend fun getTask(id: Long): Task {
		val data = tasksDao.getTask(id)
		val subtasks = subtasksDao.getSubtasksForTask(data.id)
		val task = data.toTask(subtasks.map { it.toSubtask() })
		return task.copy(reminders = remindersDao.getRemindersForTask(task.id).map { it.toReminder() })
	}

	override suspend fun saveCategory(category: TaskCategory) {
		categoriesDao.upsertCategory(category.toData())
	}

	override suspend fun saveSubtasks(subtasks: List<Subtask>) {
		subtasksDao.upsertSubtasks(subtasks.map { it.toData() })
	}

	override suspend fun deleteSubtasksForTask(id: Long) {
		subtasksDao.deleteSubtasksByTask(id)
	}

	override suspend fun deleteSubtasks(subtasks: List<Subtask>) {
		subtasksDao.deleteSubtasks(subtasks.map { it.toData() })
	}

	override fun getAllTasks(): Flow<List<Task>> {
		val subtasks = subtasksDao
			.getAllSubtasks()
			.map { it.map { it.toSubtask() } }
		val reminders = remindersDao
			.getAllReminders()
			.map { it.map { it.toReminder() } }

		return tasksDao
			.getAllTasks()
			.map { it.map { it.toTask() } }
			.combine(subtasks) { tasks, subtasks ->
				tasks.map { task ->
					task.copy(subtasks = subtasks.filter { it.parentTaskId == task.id })
				}
			}
			.combine(reminders) { tasks, reminders ->
				tasks.map { task ->
					task.copy(reminders = reminders.filter { it.parentTaskId == task.id })
				}
			}
	}

	override suspend fun getRepeatingTasks(): List<Task> {
		return tasksDao.getRepeatingTasks().map { it.toTask() }
	}

	override suspend fun getCompletedTasks(): List<Task> {
		return tasksDao.getCompletedTasks().map { it.toTask() }
	}

	override suspend fun getYesterdayUncompletedTasks(): List<Task> {
		return tasksDao
			.getUncompletedTasks()
			.map { it.toTask() }
			.filter { task ->
				task.dueDate?.toLocalDate()?.isEqual(Time.yesterday()) == true
			}
	}

	override suspend fun getUncompletedDatelessTasks(): List<Task> {
		return tasksDao
			.getUncompletedTasks()
			.map { it.toTask() }
			.filter { it.dueDate == null }
	}

	override fun getDeletedTasks(): Flow<List<Task>> {
		val subtasks = subtasksDao
			.getAllSubtasks()
			.map { it.map { it.toSubtask() } }

		return tasksDao
			.getDeletedTasks()
			.combine(subtasks) { tasks, subtasks ->
				tasks.map { taskData ->
					taskData.toTask(subtasks.filter { it.parentTaskId == taskData.id })
				}
			}
	}

	override fun getAllCategories(): Flow<List<TaskCategory>> {
		return categoriesDao
			.getAllCategories()
			.map { it.map { it.toTaskCategory() } }
			.combine(getAllTasks()) { categories, tasks ->
				categories.map { category ->
					category.copy(tasks = tasks.filter { it.categoryId == category.id })
				}
			}
	}

	override suspend fun getCategory(id: Long): TaskCategory {
		return categoriesDao.getCategory(id).toTaskCategory()
	}

	override suspend fun saveCategories(categories: List<TaskCategory>) {
		categoriesDao.saveCategories(categories.map { it.toData() })
	}

	override suspend fun getTaskCountForCategory(id: Long): Int {
		return categoriesDao.getTaskCountForCategory(id)
	}

	override suspend fun getDefaultCategory(): TaskCategory {
		return categoriesDao.getDefaultCategory().toTaskCategory()
	}

	override suspend fun deleteCategory(id: Long) {
		categoriesDao.deleteCategory(id)
	}

	override suspend fun saveReminders(reminders: List<Reminder>) {
		remindersDao.saveReminders(reminders.map { it.toData() })
	}

	override suspend fun getRemindersForTask(id: Long): List<Reminder> {
		return remindersDao.getRemindersForTask(id).map { it.toReminder() }
	}
}