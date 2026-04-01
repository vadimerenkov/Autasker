package vadimerenkov.autasker.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

actual class AppDatabase(
	private val context: Context
) : RoomDatabaseConstructor<TasksDatabase> {

	actual override fun initialize(): TasksDatabase {

		val path = context.getDatabasePath("tasks.db")

		return Room.databaseBuilder(
			context = context,
			klass = TasksDatabase::class.java,
			name = "tasks.db"
		)
			.setDriver(BundledSQLiteDriver())
			.setQueryCoroutineContext(Dispatchers.IO)
			.build()
	}
}