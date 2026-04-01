package vadimerenkov.autasker.data

import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import ca.gosyer.appdirs.AppDirs
import kotlinx.coroutines.Dispatchers
import java.io.File

actual class AppDatabase : RoomDatabaseConstructor<TasksDatabase> {

	actual override fun initialize(): TasksDatabase {
		val appdirs = AppDirs {
			appName = "Autasker"
		}
		val path = appdirs.getUserDataDir()

		val file = File(path, "database/tasks.db")

		return Room.databaseBuilder<TasksDatabase>(file.absolutePath)
			.setDriver(BundledSQLiteDriver())
			.setQueryCoroutineContext(Dispatchers.IO)
			.build()
	}
}