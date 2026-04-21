package vadimerenkov.autasker.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import vadimerenkov.autasker.common.data.dao.CategoriesDao
import vadimerenkov.autasker.common.data.dao.JobsDao
import vadimerenkov.autasker.common.data.dao.PagesDao
import vadimerenkov.autasker.common.data.dao.RemindersDao
import vadimerenkov.autasker.common.data.dao.SubtasksDao
import vadimerenkov.autasker.common.data.dao.TasksDao
import vadimerenkov.autasker.common.data.mappers.Converters

@Database(
	entities = [
		CategoryData::class,
		TaskData::class,
		SubtaskData::class,
		ReminderData::class,
		JobData::class,
		PageData::class
	],
	version = 1
)
@TypeConverters(Converters::class)
abstract class TasksDatabase: RoomDatabase() {

	abstract fun tasksDao(): TasksDao
	abstract fun subtasksDao(): SubtasksDao
	abstract fun remindersDao(): RemindersDao
	abstract fun categoriesDao(): CategoriesDao
	abstract fun jobsDao(): JobsDao
	abstract fun pagesDao(): PagesDao
}

expect class AppDatabase : RoomDatabaseConstructor<TasksDatabase> {

	override fun initialize(): TasksDatabase
}