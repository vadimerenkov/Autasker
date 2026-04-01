package vadimerenkov.autasker.settings

import io.github.vinceglb.autolaunch.AutoLaunch

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