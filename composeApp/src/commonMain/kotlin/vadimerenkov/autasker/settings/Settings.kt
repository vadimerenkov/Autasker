package vadimerenkov.autasker.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import vadimerenkov.autasker.domain.Sorting
import vadimerenkov.autasker.domain.Time
import vadimerenkov.autasker.domain.toZonedDateTime
import vadimerenkov.autasker.settings.enums.DateFormat
import vadimerenkov.autasker.settings.enums.Language
import vadimerenkov.autasker.settings.enums.Theme
import vadimerenkov.autasker.settings.enums.TimeFormat
import java.time.DayOfWeek
import java.time.LocalTime


class Settings(
	val dataStore: DataStore<Preferences>,
	private val applicationScope: CoroutineScope
) {
	var state by mutableStateOf(SettingsState())
		private set

	init {
		dataStore.data
			.map { prefs ->
				SettingsState(
					endOfDayTime = LocalTime.ofSecondOfDay(prefs[END_OF_DAY_TIME]?.toLong() ?: 0),
					autoDeleteFromTrash = prefs[AUTODELETE_FROM_TRASH] ?: true,
					daysUntilDeleteFromTrash = prefs[DELETE_FROM_TRASH_DAYS] ?: 30,
					autoDeleteCompleted = prefs[AUTODELETE_COMPLETED] ?: true,
					daysUntilDeleteCompleted = prefs[DELETE_COMPLETED_DAYS] ?: 7,
					firstDayOfWeek = DayOfWeek.of(prefs[FIRST_DAY_OF_WEEK] ?: 1),
					language = if (prefs[LANGUAGE] == null) null else Language.valueOf(prefs[LANGUAGE]!!),
					theme = Theme.valueOf(prefs[THEME] ?: Theme.DEVICE.name),
					timeFormat = TimeFormat.valueOf(prefs[TIME_FORMAT] ?: TimeFormat.CLOCK_24.name),
					dateFormat = DateFormat.valueOf(prefs[DATE_FORMAT] ?: DateFormat.DDMMYYYY.name),
					closeToTray = prefs[TRAY] ?: false,
					autoLaunch = prefs[AUTOLAUNCH] ?: false,
					playSound = prefs[PLAY_SOUND] ?: true
				)
			}
			.onEach { state = it }
			.launchIn(applicationScope)
	}

	suspend fun <T> saveSetting(key: Preferences.Key<T>, data: T) {
		dataStore.putData(key, data)
	}

	suspend fun saveExitTime() {
		val nowSeconds = Time.now().toEpochSecond()
		dataStore.putData(LAST_EXIT_TIME, nowSeconds)
	}

	suspend fun saveTodaySorting(sorting: Sorting) {
		dataStore.putData(TODAY_SORTING, sorting.name)
	}

	suspend fun saveTomorrowSorting(sorting: Sorting) {
		dataStore.putData(TOMORROW_SORTING, sorting.name)
	}

	suspend fun saveTodayCompletedShowing(isShowing: Boolean) {
		dataStore.putData(SHOW_TODAY_COMPLETED, isShowing)
	}

	suspend fun saveTomorrowCompletedShowing(isShowing: Boolean) {
		dataStore.putData(SHOW_TOMORROW_COMPLETED, isShowing)
	}

	suspend fun getTodayShowCompleted(): Boolean {
		return dataStore.data.first()[SHOW_TODAY_COMPLETED] ?: true
	}

	suspend fun getTomorrowShowCompleted(): Boolean {
		return dataStore.data.first()[SHOW_TOMORROW_COMPLETED] ?: true
	}

	suspend fun getTodaySorting(): Sorting {
		val sorting = dataStore.data.first()[TODAY_SORTING] ?: Sorting.BY_DATE_ASCENDING.name
		return Sorting.valueOf(sorting)
	}

	suspend fun getTomorrowSorting(): Sorting {
		val sorting = dataStore.data.first()[TOMORROW_SORTING] ?: Sorting.BY_DATE_ASCENDING.name
		return Sorting.valueOf(sorting)
	}

	suspend fun getLocale(): Language? {
		val language = dataStore.data.first()[LANGUAGE] ?: return null
		return Language.valueOf(language)
	}

	suspend fun checkForNewDay(): Boolean {
		val exitTime = dataStore.getFlowOrNull(LAST_EXIT_TIME).first().toZonedDateTime()
		val isNew = exitTime?.dayOfMonth != Time.today().dayOfMonth
//		println("Recorded date is ${exitTime?.dayOfMonth}, today is ${Time.today()}")

		println("Exit time was $exitTime, so newday is $isNew")
		return isNew
	}
}