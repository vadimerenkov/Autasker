package vadimerenkov.autasker.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import vadimerenkov.autasker.core.database.CategoryData

@Dao
interface CategoriesDao {

	@Upsert
	suspend fun upsertCategory(category: CategoryData)

	@Upsert
	suspend fun saveCategories(categories: List<CategoryData>)

	@Query("SELECT * FROM categorydata WHERE isDeleted = 0 ORDER BY `index`")
	fun getAllCategories(): Flow<List<CategoryData>>

	@Query("SELECT * FROM categorydata WHERE id = :id")
	suspend fun getCategory(id: Long): CategoryData

	@Query("SELECT COUNT(*) FROM taskdata WHERE categoryId = :id")
	suspend fun getTaskCountForCategory(id: Long): Int

	@Query("SELECT * FROM categorydata WHERE isDefault = 1")
	suspend fun getDefaultCategory(): CategoryData

	@Query("DELETE FROM categorydata WHERE id = :id")
	suspend fun deleteCategory(id: Long)
}