package vadimerenkov.autasker.common.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal val END_OF_DAY_TIME = intPreferencesKey("END_OF_DAY_TIME")
internal val LAST_EXIT_TIME = longPreferencesKey("LAST_EXIT_TIME")
internal val AUTODELETE_FROM_TRASH = booleanPreferencesKey("AUTODELETE")
internal val AUTODELETE_COMPLETED = booleanPreferencesKey("AUTODELETE_COMPLETED")
internal val DELETE_FROM_TRASH_DAYS = longPreferencesKey("DELETE_DAYS")
internal val DELETE_COMPLETED_DAYS = longPreferencesKey("DELETE_COMPLETED_DAYS")
internal val FIRST_DAY_OF_WEEK = intPreferencesKey("FIRST_DAY_OF_WEEK")
internal val LANGUAGE = stringPreferencesKey("LANGUAGE")
internal val THEME = stringPreferencesKey("THEME")
internal val TIME_FORMAT = stringPreferencesKey("TIME FORMAT")
internal val DATE_FORMAT = stringPreferencesKey("DATE_FORMAT")
internal val TRAY = booleanPreferencesKey("TRAY")
internal val TODAY_SORTING = stringPreferencesKey("TODAY_SORTING")
internal val TOMORROW_SORTING = stringPreferencesKey("TOMORROW_SORTING")
internal val SHOW_TODAY_COMPLETED = booleanPreferencesKey("SHOW_TODAY_COMPLETED")
internal val SHOW_TOMORROW_COMPLETED = booleanPreferencesKey("SHOW_TOMORROW_COMPLETED")
internal val AUTOLAUNCH = booleanPreferencesKey("AUTOLAUNCH")
internal val PLAY_SOUND = booleanPreferencesKey("PLAY_SOUND")
internal val THEME_COLOR = stringPreferencesKey("THEME_COLOR")