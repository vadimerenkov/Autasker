package vadimerenkov.autasker.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.core.database.ReminderData

@Dao
interface RemindersDao {

	@Upsert
	suspend fun saveReminders(reminders: List<ReminderData>)

	@Query("SELECT * FROM reminderdata")
	fun getAllReminders(): Flow<List<ReminderData>>

	@Query("SELECT * FROM reminderdata WHERE parentTaskId = :id")
	suspend fun getRemindersForTask(id: Long): List<ReminderData>

	@Query("DELETE FROM ReminderData WHERE parentTaskId = :parentTaskId")
	suspend fun deleteRemindersByTask(parentTaskId: Long)
}