package vadimerenkov.autasker.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import vadimerenkov.autasker.domain.RepeatState

@Entity
data class TaskData(
	@PrimaryKey(autoGenerate = true) val id: Long,
	val categoryId: Long,
	val index: Int,
	val title: String,
	val description: String? = null,
	val dueDateEpochSeconds: Long? = null,
	@Embedded val repeatState: RepeatState = RepeatState(),
	val isCompleted: Boolean = false,
	val isDeleted: Boolean = false,
	val deletedDateEpochSeconds: Long? = null,
	val completedDateEpochSeconds: Long? = null,
	val isAllDay: Boolean = true,
	val importance: Int = 0
)
