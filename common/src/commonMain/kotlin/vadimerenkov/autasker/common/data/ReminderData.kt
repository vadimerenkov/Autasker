package vadimerenkov.autasker.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import vadimerenkov.autasker.common.domain.Period

@Entity
data class ReminderData(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val parentTaskId: Long,
	val period: String = Period.DAY.name,
	val times: Long = 0,
	val hour: Int = 12,
	val minute: Int = 0
)
