package vadimerenkov.autasker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import vadimerenkov.autasker.core.database.dao.CategoriesDao
import vadimerenkov.autasker.core.database.dao.JobsDao
import vadimerenkov.autasker.core.database.dao.PagesDao
import vadimerenkov.autasker.core.database.dao.RemindersDao
import vadimerenkov.autasker.core.database.dao.SubtasksDao
import vadimerenkov.autasker.core.database.dao.TasksDao
import vadimerenkov.autasker.core.database.mappers.Converters

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