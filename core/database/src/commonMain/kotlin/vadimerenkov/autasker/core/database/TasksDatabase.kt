package vadimerenkov.autasker.core.database

import androidx.room.AutoMigration
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
import vadimerenkov.autasker.core.database.habits.HabitCompletionData
import vadimerenkov.autasker.core.database.habits.HabitData
import vadimerenkov.autasker.core.database.habits.HabitsDao
import vadimerenkov.autasker.core.database.mappers.Converters

@Database(
	entities = [
		CategoryData::class,
		TaskData::class,
		SubtaskData::class,
		ReminderData::class,
		JobData::class,
		PageData::class,
		HabitData::class,
		HabitCompletionData::class
	],
	version = 2,
	autoMigrations = [
		AutoMigration(1, 2)
	],
	exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TasksDatabase: RoomDatabase() {

	abstract fun tasksDao(): TasksDao
	abstract fun subtasksDao(): SubtasksDao
	abstract fun remindersDao(): RemindersDao
	abstract fun categoriesDao(): CategoriesDao
	abstract fun jobsDao(): JobsDao
	abstract fun pagesDao(): PagesDao
	abstract fun habitsDao(): HabitsDao
}

expect class AppDatabase : RoomDatabaseConstructor<TasksDatabase> {

	override fun initialize(): TasksDatabase
}