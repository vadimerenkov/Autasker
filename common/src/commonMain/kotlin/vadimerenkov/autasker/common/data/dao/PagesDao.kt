package vadimerenkov.autasker.common.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.common.data.PageData

@Dao
interface PagesDao {
	@Upsert
	suspend fun savePage(page: PageData)

	@Query("SELECT * FROM pagedata ORDER BY `index`")
	fun getAllPages(): Flow<List<PageData>>

	@Query("DELETE FROM pagedata WHERE id = :id")
	suspend fun deletePage(id: Long)
}