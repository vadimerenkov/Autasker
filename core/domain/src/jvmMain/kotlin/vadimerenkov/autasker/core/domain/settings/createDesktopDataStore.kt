package vadimerenkov.autasker.core.domain.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

fun createDesktopDataStore(): DataStore<Preferences> {
	return createDataStore(
		producePath = {
			val file =
				File(System.getProperty("java.io.tmpdir"), "autasker_preferences.preferences_pb")
			file.absolutePath
		}
	)
}