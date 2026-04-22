package vadimerenkov.autasker.core.data.settings

actual object AutoLaunch {

	val autoLaunch = AutoLaunch("vadimerenkov.autasker")

	actual suspend fun enable() {
		autoLaunch.enable()
		println("Enabled autolaunch")
	}

	actual suspend fun disable() {
		autoLaunch.disable()
		println("Disabled autolaunch")
	}
}