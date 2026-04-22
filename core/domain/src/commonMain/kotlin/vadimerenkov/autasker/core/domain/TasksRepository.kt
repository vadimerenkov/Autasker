package vadimerenkov.autasker.core.domain

import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.core.domain.reminders.Reminder
import java.time.ZonedDateTime

interface TasksRepository {
	suspend fun saveTask(task: Task): Long
	suspend fun saveTasks(tasks: List<Task>)
	suspend fun deleteTasks(tasks: List<Task>)
	suspend fun getTask(id: Long): Task
	suspend fun saveCategory(category: TaskCategory)
	suspend fun saveSubtasks(subtasks: List<Subtask>)
	suspend fun deleteSubtasksForTask(id: Long)
	suspend fun deleteSubtasks(subtasks: List<Subtask>)
	fun getAllTasks(): Flow<List<Task>>
	suspend fun getRepeatingTasks(): List<Task>
	suspend fun getCompletedTasks(): List<Task>
	suspend fun getYesterdayUncompletedTasks(): List<Task>
	suspend fun getUncompletedDatelessTasks(): List<Task>
	fun getDeletedTasks(): Flow<List<Task>>
	fun getAllCategories(): Flow<List<TaskCategory>>
	suspend fun getCategory(id: Long): TaskCategory
	suspend fun saveCategories(categories: List<TaskCategory>)
	suspend fun getTaskCountForCategory(id: Long): Int
	suspend fun getDefaultCategory(): TaskCategory
	suspend fun deleteCategory(id: Long)
	suspend fun saveReminders(reminders: List<Reminder>)
	suspend fun getRemindersForTask(id: Long): List<Reminder>
	fun getAllReminders(): Flow<Map<Task, List<ZonedDateTime>>>
	suspend fun deleteRemindersForTask(id: Long)
	suspend fun getAllJobs(): List<ReminderJob>
	suspend fun saveJob(job: ReminderJob)
	suspend fun getJobsForTask(id: Long): List<ReminderJob>
	suspend fun deleteJob(jobKey: String)
	fun getAllPages(): Flow<List<Page>>
	suspend fun savePage(page: Page)
	suspend fun deletePage(id: Long)
}