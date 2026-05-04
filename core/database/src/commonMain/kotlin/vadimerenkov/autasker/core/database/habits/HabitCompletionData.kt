package vadimerenkov.autasker.core.database.habits

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class HabitCompletionData(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val habitId: Long,
	val date: Instant,
	val quantity: Int
)
