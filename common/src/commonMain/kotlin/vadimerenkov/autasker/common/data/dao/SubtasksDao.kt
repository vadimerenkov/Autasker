package vadimerenkov.autasker.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.common.data.SubtaskData

@Dao
interface SubtasksDao {

	@Upsert
	suspend fun upsertSubtasks(subtasks: List<SubtaskData>)

	@Delete
	suspend fun deleteSubtasks(subtasks: List<SubtaskData>)

	@Query("DELETE FROM subtaskdata WHERE parentTaskId = :parentTaskId")
	suspend fun deleteSubtasksByTask(parentTaskId: Long)

	@Query("SELECT * FROM subtaskdata")
	fun getAllSubtasks(): Flow<List<SubtaskData>>

	@Query("SELECT * FROM subtaskdata WHERE parentTaskId = :parentTaskId")
	suspend fun getSubtasksForTask(parentTaskId: Long): List<SubtaskData>
}