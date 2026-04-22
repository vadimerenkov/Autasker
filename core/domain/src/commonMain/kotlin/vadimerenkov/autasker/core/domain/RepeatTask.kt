package vadimerenkov.autasker.core.domain

/*
fun Task.getDatesForPeriod(start: ZonedDateTime, end: ZonedDateTime): List<ZonedDateTime> {
//	println("Start date: $start, end date: $end")

	val list = mutableListOf<ZonedDateTime>()

	if (dueDate == null) return list.toList()

	println("List of dates1 for $title: $list")

	if (dueDate.isAfter(start) && dueDate.isBefore(end)) {
		list.add(dueDate)
	}

	println("List of dates2 for $title: $list")

	if (!repeatState.isRepeating) return list.toList()

	var dateTime = dueDate!!

	while (dateTime.isBefore(start)) {
		dateTime = dateTime.calculateNewDate(repeatState)
		if (dateTime.isAfter(start)) {
			if (dateTime.isBefore(end)) {
				list.add(dateTime)
			}
			break
		}
	}

	println("List of dates3 for $title: $list")

	while (true) {
		val date = dateTime.calculateNewDate(repeatState)
		if (date.isAfter(end)) {
			break
		}
		list.add(date)
		dateTime = date
	}

	println("List of dates4 for $title: $list")

	return list.toList()
}

fun Task.calculateListOfDates(): List<ZonedDateTime?> {
	if (dueDate == null) return listOf(null)
	if (!repeatState.isRepeating) return listOf(dueDate)

	val list = mutableListOf(dueDate)
	var nextDate = dueDate.calculateNewDate(repeatState)
	list.add(nextDate)
	while (list.last().isBefore(Time.tomorrowEnd()) || list.size < 50) {
		nextDate = nextDate.calculateNewDate(repeatState)
		list.add(nextDate)
	}

	return list.toList()
}

 */