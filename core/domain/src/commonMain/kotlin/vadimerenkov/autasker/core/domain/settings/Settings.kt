package vadimerenkov.autasker.core.domain.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import vadimerenkov.autasker.core.domain.Sorting
import vadimerenkov.autasker.core.domain.Time
import vadimerenkov.autasker.core.domain.settings.enums.DateFormat
import vadimerenkov.autasker.core.domain.settings.enums.Language
import vadimerenkov.autasker.core.domain.settings.enums.Theme
import vadimerenkov.autasker.core.domain.settings.enums.ThemeColor
import vadimerenkov.autasker.core.domain.settings.enums.TimeFormat
import vadimerenkov.autasker.core.domain.toZonedDateTime
import java.time.DayOfWeek
import java.time.LocalTime


class Settings(
	val dataStore: DataStore<Preferences>,
	private val applicationScope: CoroutineScope
) {
	val _state = MutableStateFlow(SettingsState())
	val state = _state.asStateFlow()

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
					themeColor = ThemeColor.valueOf(prefs[THEME_COLOR] ?: ThemeColor.BLUE.name),
					timeFormat = TimeFormat.valueOf(prefs[TIME_FORMAT] ?: TimeFormat.CLOCK_24.name),
					dateFormat = DateFormat.valueOf(prefs[DATE_FORMAT] ?: DateFormat.DDMMYYYY.name),
					closeToTray = prefs[TRAY] ?: false,
					autoLaunch = prefs[AUTOLAUNCH] ?: false,
					playSound = prefs[PLAY_SOUND] ?: true
				)
			}
			.onEach { newState ->
				_state.update { newState }
			}
			.launchIn(applicationScope)
	}

	suspend fun <T> saveSetting(key: Preferences.Key<T>, data: T) {
		dataStore.putData(key, data)
	}

	suspend fun saveTimeFormat(timeFormat: TimeFormat) {
		dataStore.putData(TIME_FORMAT, timeFormat.name)
	}

	suspend fun saveDateFormat(dateFormat: DateFormat) {
		dataStore.putData(DATE_FORMAT, dateFormat.name)
	}

	suspend fun saveLanguage(language: Language) {
		dataStore.putData(LANGUAGE, language.name)
	}

	suspend fun saveFirstDayOfWeek(day: Int) {
		dataStore.putData(FIRST_DAY_OF_WEEK, day)
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