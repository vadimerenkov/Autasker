package vadimerenkov.autasker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import vadimerenkov.autasker.domain.Sorting

@Entity
data class CategoryData(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val title: String? = null,
	val sorting: Sorting = Sorting.MANUAL,
	val isDefault: Boolean = false,
	val isDeleted: Boolean = false,
	val index: Int,
	val completedOpen: Boolean = true,
	val pageId: Long = 1
)
