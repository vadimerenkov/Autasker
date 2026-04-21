package vadimerenkov.autasker.common.settings

expect object AutoLaunch {

	suspend fun enable()
	suspend fun disable()
}