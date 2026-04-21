package vadimerenkov.autasker.common

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform