package vadimerenkov.autasker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform