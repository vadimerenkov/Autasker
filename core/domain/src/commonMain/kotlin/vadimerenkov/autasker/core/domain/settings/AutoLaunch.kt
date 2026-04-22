package vadimerenkov.autasker.core.domain.settings

expect object AutoLaunch {

	suspend fun enable()
	suspend fun disable()
}