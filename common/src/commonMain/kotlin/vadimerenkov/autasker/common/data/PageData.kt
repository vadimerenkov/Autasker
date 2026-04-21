package vadimerenkov.autasker.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PageData(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val title: String? = null,
	val index: Int = 0
)
