package vadimerenkov.autasker.core.domain

import java.time.Instant

data class ReminderJob(
	val key: String,
	val parentTaskId: Long,
	val triggerDate: Instant
)
