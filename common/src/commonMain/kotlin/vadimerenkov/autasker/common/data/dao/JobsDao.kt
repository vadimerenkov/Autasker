package vadimerenkov.autasker.common.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import vadimerenkov.autasker.common.data.JobData

@Dao
interface JobsDao {

	@Upsert
	suspend fun saveJob(job: JobData)

	@Query("SELECT * FROM jobdata")
	suspend fun getAllJobs(): List<JobData>

	@Query("SELECT * FROM jobdata WHERE parentTaskId = :id")
	suspend fun getJobsForTask(id: Long): List<JobData>

	@Query("DELETE FROM jobdata WHERE `key` = :jobKey")
	suspend fun deleteJob(jobKey: String)
}