package vadimerenkov.autasker.core.database.habits

import androidx.room.Entity
import androidx.room.PrimaryKey
import vadimerenkov.autasker.core.domain.Period
import vadimerenkov.autasker.core.domain.habits.HabitType

@Entity
data class HabitData(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val title: String = "",
	val period: Period = Period.DAY,
	val times: Int = 1,
	val type: HabitType = HabitType.SINGLE,
	val customQuantifier: String? = null
)
