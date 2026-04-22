package vadimerenkov.autasker.core.domain.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T> DataStore<Preferences>.getFlow(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
	return this.data
		.map { it[key] ?: defaultValue }
}

fun <T> DataStore<Preferences>.getFlowOrNull(key: Preferences.Key<T>): Flow<T?> {
	return this.data
		.map { it[key] }
}

suspend fun <T> DataStore<Preferences>.putData(key: Preferences.Key<T>, data: T) {
	edit { prefs ->
		prefs[key] = data
	}
}