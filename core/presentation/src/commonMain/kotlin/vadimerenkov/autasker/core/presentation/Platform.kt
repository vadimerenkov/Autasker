package vadimerenkov.autasker.core.presentation

enum class Platform {
	DESKTOP,
	ANDROID
}

expect fun getPlatform(): Platform