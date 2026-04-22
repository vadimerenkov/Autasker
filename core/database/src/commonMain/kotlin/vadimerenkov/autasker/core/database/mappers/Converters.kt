package vadimerenkov.autasker.core.database.mappers

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.Instant

class Converters {

	@TypeConverter
	fun listOfWeekDaysToString(list: List<DayOfWeek>): String {
		return String(list.map { dayOfWeek ->
			dayOfWeek.value.digitToChar()
		}.toCharArray())
	}

	@TypeConverter
	fun stringToListOfWeekDays(string: String): List<DayOfWeek> {
		val ints = string.map { char ->
			char.digitToInt()
		}
		return ints.map { value ->
			DayOfWeek.of(value)
		}
	}

	@TypeConverter
	fun instantToLong(instant: Instant): Long {
		return instant.epochSecond
	}

	@TypeConverter
	fun longToInstant(long: Long): Instant {
		return Instant.ofEpochSecond(long)
	}
}