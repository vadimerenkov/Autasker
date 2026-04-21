package vadimerenkov.autasker.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class JobData(
	@PrimaryKey val key: String,
	val parentTaskId: Long,
	val triggerDate: Instant
)
