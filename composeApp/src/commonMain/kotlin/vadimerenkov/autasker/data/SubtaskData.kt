package vadimerenkov.autasker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubtaskData(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val parentTaskId: Long,
	val title: String,
	val isCompleted: Boolean = false,
	val index: Int
)
