package vadimerenkov.autasker.core.domain.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val END_OF_DAY_TIME = intPreferencesKey("END_OF_DAY_TIME")
val LAST_EXIT_TIME = longPreferencesKey("LAST_EXIT_TIME")
val AUTODELETE_FROM_TRASH = booleanPreferencesKey("AUTODELETE")
val AUTODELETE_COMPLETED = booleanPreferencesKey("AUTODELETE_COMPLETED")
val DELETE_FROM_TRASH_DAYS = longPreferencesKey("DELETE_DAYS")
val DELETE_COMPLETED_DAYS = longPreferencesKey("DELETE_COMPLETED_DAYS")
val FIRST_DAY_OF_WEEK = intPreferencesKey("FIRST_DAY_OF_WEEK")
val LANGUAGE = stringPreferencesKey("LANGUAGE")
val THEME = stringPreferencesKey("THEME")
val TIME_FORMAT = stringPreferencesKey("TIME FORMAT")
val DATE_FORMAT = stringPreferencesKey("DATE_FORMAT")
val TRAY = booleanPreferencesKey("TRAY")
val TODAY_SORTING = stringPreferencesKey("TODAY_SORTING")
val TOMORROW_SORTING = stringPreferencesKey("TOMORROW_SORTING")
val SHOW_TODAY_COMPLETED = booleanPreferencesKey("SHOW_TODAY_COMPLETED")
val SHOW_TOMORROW_COMPLETED = booleanPreferencesKey("SHOW_TOMORROW_COMPLETED")
val AUTOLAUNCH = booleanPreferencesKey("AUTOLAUNCH")
val PLAY_SOUND = booleanPreferencesKey("PLAY_SOUND")
val THEME_COLOR = stringPreferencesKey("THEME_COLOR")