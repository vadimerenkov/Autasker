package vadimerenkov.autasker.settings

expect object AutoLaunch {

	suspend fun enable()
	suspend fun disable()
}