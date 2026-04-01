package vadimerenkov.autasker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.data.TaskData

@Dao
interface TasksDao {

	@Upsert
	suspend fun upsertTask(task: TaskData): Long

	@Upsert
	suspend fun upsertTasks(tasks: List<TaskData>)

	@Transaction
	@Query("SELECT * FROM taskdata WHERE id = :id")
	suspend fun getTask(id: Long): TaskData

	@Delete
	suspend fun deleteTasks(tasks: List<TaskData>)

	@Transaction
	@Query("SELECT * FROM taskdata WHERE isDeleted = 0")
	fun getAllTasks(): Flow<List<TaskData>>

	@Query("SELECT * FROM taskdata WHERE isRepeating = 1" +
			" AND dueDateEpochSeconds IS NOT NULL" +
			" AND isDeleted = 0")
	suspend fun getRepeatingTasks(): List<TaskData>

	@Query("SELECT * FROM taskdata WHERE isCompleted = 1" +
			" AND isDeleted = 0")
	suspend fun getCompletedTasks(): List<TaskData>

	@Query("SELECT * FROM taskdata WHERE isCompleted=0 AND isDeleted=0")
	suspend fun getUncompletedTasks(): List<TaskData>

	@Transaction
	@Query("SELECT * FROM taskdata WHERE isDeleted = 1")
	fun getDeletedTasks(): Flow<List<TaskData>>
}