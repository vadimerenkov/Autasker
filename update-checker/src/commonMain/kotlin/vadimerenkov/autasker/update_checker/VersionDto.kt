package vadimerenkov.autasker.update_checker

import kotlinx.serialization.Serializable

@Serializable
data class VersionDto(
	val tag_name: String
)
