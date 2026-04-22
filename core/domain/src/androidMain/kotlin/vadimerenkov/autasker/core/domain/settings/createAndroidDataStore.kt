package vadimerenkov.autasker.core.domain.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createAndroidDataStore(context: Context): DataStore<Preferences> {
	return createDataStore(
		producePath = {
			context.filesDir.resolve("autasker_preferences.preferences_pb").absolutePath
		}
	)
}